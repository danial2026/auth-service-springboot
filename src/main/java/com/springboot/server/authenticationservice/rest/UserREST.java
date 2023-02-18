package com.springboot.server.authenticationservice.rest;

import com.springboot.server.authenticationservice.dto.*;
import com.springboot.server.authenticationservice.entity.UserDetailsEntity;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;
import com.springboot.server.authenticationservice.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserREST {

    private final UserService userService;

    /**
     * register user
     *
     * @param signUpDTO
     * @return
     * @throws BusinessServiceException
     */
    @PostMapping(value = "/")
    public ResponseEntity<Void> createUser(@Valid @RequestBody SignUpDTO signUpDTO, BindingResult errors) throws BusinessServiceException {

        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(signUpDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param userIdentity
     * @return
     * @throws BusinessServiceException
     */
    @PostMapping(value = "/resend-verification-code")
    public ResponseEntity<Void> resendVerificationCode(@RequestParam(name = "userIdentity") final String userIdentity) throws BusinessServiceException {
        userService.resendVerificationCode(userIdentity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * activate the user if the code if valid
     *
     * @param activationCodeDTO
     * @return
     * @throws BusinessServiceException
     */
    @PostMapping(value = "/verify")
    public ResponseEntity<Void> verifyUser(@Valid @RequestBody ActivationCodeDTO activationCodeDTO, BindingResult errors) throws BusinessServiceException{

        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userService.verifyUser(activationCodeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param loginDTO
     * @return
     * @throws BusinessServiceException
     */
    @PostMapping(value = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO, BindingResult errors) throws BusinessServiceException {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = userService.authenticate(loginDTO);
        String jwtToken = userService.generateJWTToken(authentication);
        String refreshToken = userService.generateRefreshToken(loginDTO.getUsername(), jwtToken);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken));
    }


    /**
     * @param request
     * @return
     * @throws BusinessServiceException
     */
    @PostMapping(value = "/signin/refresh-token")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request, BindingResult errors) throws BusinessServiceException {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        String requestRefreshToken = request.getRefreshToken();
        String requestLatestToken = request.getAccessToken();
        return ResponseEntity.ok(userService.refreshToken(requestRefreshToken, requestLatestToken));
    }

    /**
     * @param userIdentity Can be phone number or email or username.
     * @return
     * @throws BusinessServiceException
     */
    @PutMapping(value = "/signin/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam(name = "userIdentity") final String userIdentity) throws BusinessServiceException {
        userService.forgotPassword(userIdentity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param resetPasswordDTO
     * @return
     * @throws BusinessServiceException
     */
    @PutMapping(value = "/signin/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, BindingResult errors) throws BusinessServiceException {

        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userService.resetPassword(resetPasswordDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param resetPasswordDTO
     * @return
     * @throws BusinessServiceException
     */
    @PutMapping(value = "/signin/change-password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetailsEntity userDetails, @Valid @RequestBody ChangePasswordDTO resetPasswordDTO, BindingResult errors) throws BusinessServiceException {

        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(resetPasswordDTO, userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @return
     * @throws BusinessServiceException
     */
    @GetMapping(value = "/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsEntity userDetails) throws BusinessServiceException {

        userService.logout(userDetails.getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * @return
     * @throws BusinessServiceException
     */
    @GetMapping(value = "/delete-account")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetailsEntity userDetails) throws BusinessServiceException {

        userService.deleteAccount(userDetails.getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * return all users (only for testing , remove for production)
     *
     * @return
     * @throws BusinessServiceException
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAll() throws BusinessServiceException {

        return ResponseEntity.ok(userService.findAll());
    }

    /**
     *  returns current user profile
     *
     * @param userDetails
     * @return
     * @throws BusinessServiceException
     */
    @GetMapping(value = "/me/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentUserProfile(@AuthenticationPrincipal UserDetailsEntity userDetails) throws BusinessServiceException {

//        return ResponseEntity.ok(userService.getUserProfile(userDetails.getUsername(), userDetails.getUsername()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * this api is used when users want to change their phone number
     *
     * @param phoneNumber
     * @return
     * @throws BusinessServiceException
     */
    @PutMapping(value = "/signin/send-verification-code-new-phone-number")
    public ResponseEntity<Void> sendVerificationCodeNewPhoneNumber(@RequestParam(name = "phoneNumber") final String phoneNumber) throws BusinessServiceException {
        userService.sendVerificationCodeNewPhoneNumber(phoneNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * this api is used when users want to change their phone number
     *
     * @param code
     * @return
     * @throws BusinessServiceException
     */
    @PutMapping(value = "/verify-new")
    public ResponseEntity<Void> updatePhoneNumber(@RequestParam(name = "code") final String code) throws BusinessServiceException {
        userService.verifyNewPhoneNumber(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
