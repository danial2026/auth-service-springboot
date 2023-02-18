package com.springboot.server.authenticationservice.enums;

/**
 * SMS_MESSAGES
 *
 * @author: Danial
 */
public enum SMSMessage {

    ENGLISH_VERIFY(" is your verification code"),
    ENGLISH_RESET_PASSWORD(" is your resetpassword code");

    private final String message;

    SMSMessage(String message) {this.message = message;}

    public String getMessage() {
        return message;
    }
}
