package com.springboot.server.authenticationservice.service.sms;

import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;

/**
 * SMSServiceException
 *
 * @author: Danial
 */
public class SMSServiceException extends BusinessServiceException {

    public SMSServiceException(ErrorMessage ErrorMessage) {
        super(ErrorMessage);
    }
}