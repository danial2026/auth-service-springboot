package com.springboot.server.authenticationservice.service.sms;

import com.springboot.server.authenticationservice.exception.BusinessServiceException;

public interface SMSService {

    /**
     * @param to
     * @param subject
     * @param content
     * @param isMultipart
     * @param isHtml
     * @throws BusinessServiceException
     */
    void send(String to, String subject, String content, boolean isMultipart, boolean isHtml) throws BusinessServiceException;
}
