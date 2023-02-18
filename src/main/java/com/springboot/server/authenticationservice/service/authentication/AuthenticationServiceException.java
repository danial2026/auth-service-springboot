package com.springboot.server.authenticationservice.service.authentication;

import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;

public class AuthenticationServiceException extends BusinessServiceException {

    public AuthenticationServiceException(ErrorMessage ErrorMessage) {
        super(ErrorMessage);
    }
}