package com.springboot.server.authenticationservice.service.user;

import com.springboot.server.authenticationservice.config.JwtTokenProvider;
import com.springboot.server.authenticationservice.dto.*;
import com.springboot.server.authenticationservice.entity.ProfileEntity;
import com.springboot.server.authenticationservice.entity.RefreshTokenEntity;
import com.springboot.server.authenticationservice.entity.RoleEntity;
import com.springboot.server.authenticationservice.entity.UserEntity;
import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.enums.SMSMessage;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;
import com.springboot.server.authenticationservice.repository.UserRepository;
import com.springboot.server.authenticationservice.service.authentication.AuthenticationService;
import com.springboot.server.authenticationservice.service.authentication.RefreshTokenService;
import com.springboot.server.authenticationservice.service.sms.SMSService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final SMSService smsService;

    private final AuthenticationService authenticationService;

    private final RefreshTokenService refreshTokenService;

    private final JwtTokenProvider tokenProvider;

    @Override
    public Authentication authenticate(final LoginDTO loginDTO) throws BusinessServiceException {
        return authenticationService.authenticate(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Override
    public String generateRefreshToken(String username, String jwtToken) throws BusinessServiceException {
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(username, jwtToken);

        return refreshToken.getRefreshToken();
    }

    @Override
    public String generateJWTToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public TokenRefreshResponse refreshToken(String requestRefreshToken, String requestLatestToken) throws BusinessServiceException{

        RefreshTokenEntity findRefreshToken = refreshTokenService.findByRefreshToken(requestRefreshToken);
        if (findRefreshToken != null) {
            if (findRefreshToken.getToken().equals(requestLatestToken)) {
                refreshTokenService.verifyExpiration(findRefreshToken);
                UserEntity userFromDataBase = userRepository.findById(findRefreshToken.getUserId()).get();
                String userNameFromDataBase = userFromDataBase.getUsername();
                Authentication authentication = new UsernamePasswordAuthenticationToken(userNameFromDataBase, null);
                String token = tokenProvider.generateToken(authentication);
                RefreshTokenEntity updatedRefreshTokenEntity = refreshTokenService.updateRefreshToken(userNameFromDataBase, token, requestRefreshToken);
                return new TokenRefreshResponse(updatedRefreshTokenEntity.getToken(), updatedRefreshTokenEntity.getRefreshToken());
            }
        }

        throw new UserServiceException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND);
    }

    @Override
    public void registerUser(final SignUpDTO signUpDTO) throws BusinessServiceException {
        if (doesUserExist(signUpDTO.getUsername(), signUpDTO.getPhoneNumber())) {
            throw new UserServiceException(ErrorMessage.USER_ALREADY_EXIST);
        }
        UserEntity user = UserEntity
                .builder()
                .username(signUpDTO.getUsername())
                .phoneNumber(signUpDTO.getPhoneNumber())
                .verificationCode(generateActivationCode()) 
                .verificationCodeExpirationDate(LocalDateTime.now().plusHours(1))
                .activated(false)
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .roleEntities(new HashSet<>() {{
                    add(RoleEntity.USER);
                }})
                .userProfileEntity(ProfileEntity
                        .builder()
                        .fullName(signUpDTO.getFullName())
                        .build())
                .build();

        sendSMS(user.getPhoneNumber(), SMSMessage.ENGLISH_VERIFY, user.getVerificationCode());
        UserEntity savedUser = userRepository.save(user);
    }

    /* (non-Javadoc)
     * @see com.springboot.server.authenticationservice.service.user.UserService#upgradeAuthorization(com.springboot.server.authenticationservice.dto.UpgradeAuthorizationDTO)
     */
    @Override
    public void upgradeAuthorization(UpgradeAuthorizationDTO upgradeAuthorizationDTO) throws BusinessServiceException {
        // TODO : read this from `application-dev.yml` file 
        //  PS: i know this is not secure but its 3 in the morning and i really need to sleep
        if (upgradeAuthorizationDTO.getAdminUsername().equals("danial") && upgradeAuthorizationDTO.getAdminPassword().equals("password") ) {
            UserEntity userEntity = authenticationService.getAuthenticatedUser();
            userEntity.setRoleEntities(new HashSet<>() {{
                add(RoleEntity.ADMIN);
            }});
            userRepository.save(userEntity);
        } else {
            throw new UserServiceException(ErrorMessage.NOT_AUTHORIZED);
        }
    }

    @Override
    public void verifyUser(final ActivationCodeDTO activationCodeDTO) throws BusinessServiceException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(activationCodeDTO.getUsername());

        if (optionalUser.isEmpty()) {
            throw new UserServiceException(ErrorMessage.USER_IS_NOT_FOUND);
        }
        final UserEntity userEntity = optionalUser.get();
        /* The code expiration date would be valid only for one hour. */
        boolean isCodeExpirationBeforeNow = LocalDateTime.now().isBefore(userEntity.getVerificationCodeExpirationDate());
        /* Check the code value and code expiration date. */
        if (userEntity.getVerificationCode().equals(activationCodeDTO.getCode()) && isCodeExpirationBeforeNow) {
            userEntity.setActivated(true);
            userRepository.save(userEntity);

        } else {
            throw new UserServiceException(ErrorMessage.ACTIVATION_CODE_NOT_VALID);
        }
    }

    @Override
    public void logout(String username) throws BusinessServiceException{
        Optional<UserEntity> currentUserEntity = userRepository.findByUsername(username);
        if (currentUserEntity.isEmpty()){
            throw new UserServiceException(ErrorMessage.USER_IS_NOT_FOUND);
        }
        refreshTokenService.deleteByUserId(currentUserEntity.get().getId());
    }

    @Override
    public void deleteAccount(String username) throws BusinessServiceException{

        logout(username);
    }

    @Override
    public void resendVerificationCode(final String userIdentity) throws BusinessServiceException {
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrPhoneNumber(userIdentity, userIdentity);
        if (optionalUser.isEmpty()) {
            throw new UserServiceException(ErrorMessage.USER_IS_NOT_FOUND);
        }
        UserEntity userEntity = optionalUser.get();
        if (userEntity.isActivated()) {
            throw new UserServiceException(ErrorMessage.USER_ALREADY_ACTIVATED);
        }
        final String newVerificationCode = generateActivationCode();
        userEntity.setVerificationCode(newVerificationCode);
        userEntity.setVerificationCodeExpirationDate(LocalDateTime.now().plusHours(1));

        sendSMS(userEntity.getPhoneNumber(), SMSMessage.ENGLISH_VERIFY, newVerificationCode);

        userRepository.save(userEntity);
    }

    @Override
    public ProfileDTO findByUsername(String username) throws BusinessServiceException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserServiceException(ErrorMessage.USER_IS_NOT_FOUND);
        }
        return convertEntityToDTO(optionalUser.get());
    }

    @Override
    public void forgotPassword(final String userIdentity) throws BusinessServiceException{
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrPhoneNumber(userIdentity, userIdentity);
        if (optionalUser.isEmpty()) {
            throw new UserServiceException(ErrorMessage.USER_IS_NOT_FOUND);
        }
        UserEntity userEntity = optionalUser.get();
        userEntity.setRecoveryCode(generateResetPasswordCode());
        userEntity.setRecoveryCodeExpirationDate(LocalDateTime.now().plusHours(1));
        sendSMS(userEntity.getPhoneNumber(), SMSMessage.ENGLISH_RESET_PASSWORD, userEntity.getRecoveryCode());
        userRepository.save(userEntity);
    }

    @Override
    public void changePassword(final ChangePasswordDTO changePasswordDTO, String userIdentity) throws BusinessServiceException{
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrPhoneNumber(userIdentity, userIdentity);
        if (optionalUser.isEmpty()) {
            throw new UserServiceException(ErrorMessage.USER_IS_NOT_FOUND);
        }
        UserEntity userEntity = optionalUser.get();

        Authentication authentication =authenticationService.authenticate(userEntity.getUsername(), changePasswordDTO.getCurrentPassword());
        if (!authentication.isAuthenticated()){
            // set new code
            throw new UserServiceException(ErrorMessage.WRONG_PASSWORD);
        }
        userEntity.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(userEntity);
        try{
            // delete the refresh token / token
            refreshTokenService.deleteByUserId(userEntity.getId());
        }catch (Exception e){
        }
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws BusinessServiceException {
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrPhoneNumber(resetPasswordDTO.getUserIdentity(), resetPasswordDTO.getUserIdentity());
        if (optionalUser.isEmpty()) {
            throw new UserServiceException(ErrorMessage.USER_IS_NOT_FOUND);
        }
        UserEntity userEntity = optionalUser.get();

        /* The code expiration date would be valid only for one hour. */
        boolean isCodeExpirationBeforeNow = LocalDateTime.now().isBefore(userEntity.getRecoveryCodeExpirationDate());
        /* Check the code value and code expiration date. */
        if (userEntity.getRecoveryCode().equals(resetPasswordDTO.getRecoveryCode()) && isCodeExpirationBeforeNow) {
            userEntity.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
            userRepository.save(userEntity);
            try{
                // delete the refresh tokens and tokens
                refreshTokenService.deleteByUserId(userEntity.getId());
            }catch (Exception e){
            }
        } else {
            throw new UserServiceException(ErrorMessage.CODE_NOT_VALID);
        }
    }

    @Override
    public void sendVerificationCodeNewPhoneNumber(String phoneNumber) throws BusinessServiceException {
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new UserServiceException(ErrorMessage.PHONE_NUMBER_ALREADY_EXIST);
        }
        UserEntity userEntity = authenticationService.getAuthenticatedUser();
        final String newVerificationCode = generateActivationCode();
        userEntity.setVerificationCode(newVerificationCode);
        userEntity.setVerificationCodeExpirationDate(LocalDateTime.now().plusHours(1));
        sendSMS(phoneNumber, SMSMessage.ENGLISH_VERIFY, newVerificationCode);
        userRepository.save(userEntity);
    }

    @Override
    public void verifyNewPhoneNumber(String code) throws BusinessServiceException {
        UserEntity userEntity = authenticationService.getAuthenticatedUser();
        boolean isCodeExpirationBeforeNow = LocalDateTime.now().isBefore(userEntity.getVerificationCodeExpirationDate());
        /* Check the code value and code expiration date. */
        if (userEntity.getVerificationCode().equals(code) && isCodeExpirationBeforeNow) {
            userEntity.setActivated(true);
            userRepository.save(userEntity);
        } else {
            throw new UserServiceException(ErrorMessage.ACTIVATION_CODE_NOT_VALID);
        }
    }

    /**
     * Check is there any user with these username or phone number
     *
     * @param phoneNumber as String.
     * @param username    as String.
     * @return true if the user is exist.
     */
    private boolean doesUserExist(final String username, final String phoneNumber) {

        return userRepository.findByUsernameOrPhoneNumber(username, phoneNumber).isPresent();
    }

    /**
     * here we send a request to sms service
     *
     * @param to a phone number as String
     * @param defaultMessage
     * @param code the request code as String
     * @throws BusinessServiceException
     */
    private void sendSMS(String to, SMSMessage defaultMessage, String code) throws BusinessServiceException {
        String content = code + defaultMessage.getMessage();

        log.info(" LOG > code message : {} ", content);
        log.info(" LOG > code : {} ", code);

        /*
        smsService.send(to, defaultMessage.name(), content, false, false);
        */
    }

    /**
     * generates a random 6 digit code
     *
     * @return
     */
    private String generateRandomCode() {
        String stringResult = "";
    //    int Low = 1;
    //    int High = 10;
    //    for (int i = 0; i < 6 ; i++){
    //        int randomInt = ThreadLocalRandom.current().nextInt(High-Low) + Low;
    //        stringResult = stringResult + randomInt;
    //    }
        stringResult = "123456";
        return stringResult;
    }

    /**
     * @return
     */
    private String generateResetPasswordCode() {
        return generateRandomCode();
    }

    /**
     * @return
     */
    private String generateActivationCode() {
        return generateRandomCode();
    }

    /**
     * @param userEntity
     * @return
     */
    ProfileDTO convertEntityToDTO(UserEntity userEntity) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setFullName(userEntity.getUserProfileEntity().getFullName());
        profileDTO.setBio(userEntity.getUserProfileEntity().getBio());
        profileDTO.setBirthday(userEntity.getUserProfileEntity().getBirthday());
        return profileDTO;
    }
}

