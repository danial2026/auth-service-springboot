package com.springboot.server.authenticationservice.exception;

import com.springboot.server.authenticationservice.enums.ErrorMessage;

/**
 * SMSServiceException
 *
 * @author: Danial
 */
public class SMSServiceException  extends BusinessServiceException {
    public SMSServiceException(ErrorMessage ErrorMessage) {
        super(ErrorMessage);
    }
}
