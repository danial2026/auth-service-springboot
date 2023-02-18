package com.springboot.server.authenticationservice.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotEnabledException extends AuthenticationException {

    public UserNotEnabledException(String message) {
        super(message);
    }

    public UserNotEnabledException(String message, Throwable t) {
        super(message, t);
    }
}