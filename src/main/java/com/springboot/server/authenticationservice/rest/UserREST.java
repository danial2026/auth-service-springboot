package com.springboot.server.authenticationservice.rest;

import com.springboot.server.authenticationservice.dto.*;
import com.springboot.server.authenticationservice.entity.UserDetailsEntity;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;
import com.springboot.server.authenticationservice.service.user.UserService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000","https://auth.danials.space"})
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserREST {

    private final UserService userService;

    @GetMapping(value = "/up")
    public ResponseEntity<?> isUp() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * register user
     *
     * @param signUpDTO
     * @return
     */
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> createUser(@Valid @RequestBody SignUpDTO signUpDTO, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            userService.registerUser(signUpDTO);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        } 
    }

    /**
     * @param userIdentity
     * @return
     */
    @PostMapping(value = "/resend-verification-code")
    public ResponseEntity<Void> resendVerificationCode(@RequestParam(name = "userIdentity") final String userIdentity) {
        try {
            userService.resendVerificationCode(userIdentity);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        } 
    }

    /**
     * activate the user if the code if valid
     *
     * @param activationCodeDTO
     * @return
     */
    @PostMapping(value = "/verify")
    public ResponseEntity<Void> verifyUser(@Valid @RequestBody ActivationCodeDTO activationCodeDTO, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            userService.verifyUser(activationCodeDTO);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }  
    }

    /**
     * @param loginDTO
     * @return
     */
    @PostMapping(value = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication authentication = userService.authenticate(loginDTO);
            String jwtToken = userService.generateJWTToken(authentication);
            String refreshToken = userService.generateRefreshToken(loginDTO.getUsername(), jwtToken);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken));

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * @param request
     * @return
     */
    @PostMapping(value = "/signin/refresh-token")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            String requestRefreshToken = request.getRefreshToken();
            String requestLatestToken = request.getAccessToken();
            return ResponseEntity.ok(userService.refreshToken(requestRefreshToken, requestLatestToken));

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param userIdentity Can be phone number or email or username.
     * @return
     */
    @PutMapping(value = "/signin/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam(name = "userIdentity") final String userIdentity) {
        try {
            userService.forgotPassword(userIdentity);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param resetPasswordDTO
     * @return
     */
    @PutMapping(value = "/signin/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            userService.resetPassword(resetPasswordDTO);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param resetPasswordDTO
     * @return
     */
    @PutMapping(value = "/signin/change-password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetailsEntity userDetails, @Valid @RequestBody ChangePasswordDTO resetPasswordDTO, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            userService.changePassword(resetPasswordDTO, userDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @return
     */
    @GetMapping(value = "/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsEntity userDetails) {
        try {
            userService.logout(userDetails.getUsername());
            return ResponseEntity.ok(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @return
     */
    @GetMapping(value = "/delete-account")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetailsEntity userDetails) {
        try {
            userService.deleteAccount(userDetails.getUsername());
            return ResponseEntity.ok(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  returns current user profile
     *
     * @param userDetails
     * @return
     */
    @GetMapping(value = "/me/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentUserProfile(@AuthenticationPrincipal UserDetailsEntity userDetails) {
        try {
            ProfileDTO profileDTO = userService.findByUsername(userDetails.getUsername());
            return ResponseEntity.ok(profileDTO);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * this api is used when users want to change their phone number
     *
     * @param phoneNumber
     * @return
     */
    @PutMapping(value = "/signin/send-verification-code-new-phone-number")
    public ResponseEntity<Void> sendVerificationCodeNewPhoneNumber(@RequestParam(name = "phoneNumber") final String phoneNumber) {
        try {
            userService.sendVerificationCodeNewPhoneNumber(phoneNumber);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * this api is used when users want to change their phone number
     *
     * @param code
     * @return
     */
    @PutMapping(value = "/verify-new")
    public ResponseEntity<Void> updatePhoneNumber(@RequestParam(name = "code") final String code) {
        try {
            userService.verifyNewPhoneNumber(code);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BusinessServiceException errorResponse) {
            return new ResponseEntity(errorResponse.getApiError(), HttpStatus.BAD_REQUEST);
        }
    }
}
