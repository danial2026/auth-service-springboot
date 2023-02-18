package com.springboot.server.authenticationservice.service.user;

import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;

public class UserServiceException extends BusinessServiceException {

    public UserServiceException(ErrorMessage ErrorMessage) {
        super(ErrorMessage);
    }
}