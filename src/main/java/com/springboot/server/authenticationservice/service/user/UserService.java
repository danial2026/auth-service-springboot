package com.springboot.server.authenticationservice.service.user;

import com.springboot.server.authenticationservice.dto.*;
import com.springboot.server.authenticationservice.entity.UserEntity;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.Optional;

/**
 * UserService
 *
 * @author: Danial
 */

public interface UserService {

    /**
     * @param loginDTO include username and password as String
     * @return
     * @throws AuthenticationException
     */
    Authentication authenticate(LoginDTO loginDTO) throws BusinessServiceException;

    /** check the refresh token validation and if valid ,generate a new set of tokens and remove the old ones and save them to redis db
     *
     * @param requestRefreshToken
     * @param requestLatestToken
     * @return
     * @throws BusinessServiceException
     */
    TokenRefreshResponse refreshToken(String requestRefreshToken, String requestLatestToken) throws BusinessServiceException;

    /**
     * generate JWT token from Authentication
     *
     * @param authentication
     * @return
     * @throws BusinessServiceException
     */
    String generateJWTToken(Authentication authentication) throws BusinessServiceException ;

    /**
     * generate refresh token as a random uuid from username and new jwtToken and
     * save the new tokens to the redis data base
     *
     * @param username
     * @param jwtToken
     * @return
     * @throws BusinessServiceException
     */
    String generateRefreshToken(String username, String jwtToken) throws BusinessServiceException ;

    /**
     * register a user if all info are valid
     *
     * @param signUpDTO
     * @throws BusinessServiceException
     */
    void registerUser(SignUpDTO signUpDTO) throws BusinessServiceException;

    /**
     * update the users authorization
     *
     * @param upgradeAuthorizationDTO include adminUsername and adminPassword as String
     * @throws BusinessServiceException
     */
    void upgradeAuthorization(UpgradeAuthorizationDTO upgradeAuthorizationDTO) throws BusinessServiceException;

    /**
     * Verify user with activation code.
     *
     * @param activationCodeDTO include username and activation code as String.
     * @throws BusinessServiceException
     */
    void verifyUser(ActivationCodeDTO activationCodeDTO) throws BusinessServiceException;

    /**
     * this function will generate a ne verification Code and send it to the user phone number
     *
     * @param userIdentity as String of username or phone number
     * @throws BusinessServiceException
     */
    void resendVerificationCode(String userIdentity) throws BusinessServiceException;

    /**
     * @param username as String
     * @return
     * @throws BusinessServiceException
     */
    Optional<UserEntity> findByUsername(String username) throws BusinessServiceException;

    /**
     * returns all user entities
     *
     * @return
     * @throws BusinessServiceException
     */
    List<UserEntity> findAll() throws BusinessServiceException;

    /**
     * first check if a user with this userIdentity exist then it will generate a new recoveryCode and send a request to SMS service to send it
     *
     * @param userIdentity as String
     * @return
     * @throws BusinessServiceException
     */
    void forgotPassword(String userIdentity) throws BusinessServiceException;

    /**
     * chnage password after login
     *
     * @param changePasswordDTO
     * @return
     * @throws BusinessServiceException
     */
    void changePassword(ChangePasswordDTO changePasswordDTO, String userIdentity) throws BusinessServiceException;

    /**
     * this function used for login out of a account
     * it means it will remove the tokens from repository
     *
     * @throws BusinessServiceException
     */
    void logout(String username) throws BusinessServiceException;

    /**
     * send a new verification code for users new phone number
     *
     * @param username as String
     * @throws BusinessServiceException
     */
    void sendVerificationCodeNewPhoneNumber(String username) throws BusinessServiceException;

    /**
     * it take the verification code as input and check if its valid and the expiration date is not passed
     * if there was an issue with the code it will throw a UserServiceException exception
     *
     * @param code as String
     * @throws BusinessServiceException
     */
    void verifyNewPhoneNumber(String code) throws BusinessServiceException;

    /**
     * delete account
     *
     * @param username
     * @throws BusinessServiceException
     */
    void deleteAccount(String username) throws BusinessServiceException;

    /**
     * checks if the recovery code is valid (its right and not expired) the save the new password in database
     * and it will throw a UserServiceException exception if anything went wrong
     * *
     * @param resetPasswordDTO include userIdentity, newPassword and recoveryCode as String
     */
    void resetPassword(ResetPasswordDTO resetPasswordDTO) throws BusinessServiceException ;
}
