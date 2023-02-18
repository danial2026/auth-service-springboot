package com.springboot.server.authenticationservice.service.sms;

import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;
import com.springboot.server.authenticationservice.exception.SMSServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.kavenegar.sdk.KavenegarApi;
import com.kavenegar.sdk.excepctions.*;
import com.kavenegar.sdk.models.*;

@Service
public class SMSServiceImpl implements SMSService {

    @Value("${kavenegar.api-key}")
    private String apiKey;

    @Value("${kavenegar.first-sender-number}")
    private String firstSenderNumber;

    private static KavenegarApi kavenegarApi;

    public void send(String to, String subject, String content, boolean isMultipart, boolean isHtml) throws BusinessServiceException {
        try {
            kavenegarApi = new KavenegarApi(apiKey);
            SendResult Result = kavenegarApi.send(firstSenderNumber, to, content);
        } catch ( HttpException | ApiException ex) {
            throw new SMSServiceException(ErrorMessage.MESSAGE_NOT_SENT);
        }
    }
}
