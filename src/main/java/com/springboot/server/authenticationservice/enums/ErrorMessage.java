package com.springboot.server.authenticationservice.enums;

/**
 * ErrorMessage
 *
 * @author: Danial
 */

public enum ErrorMessage {

    INVALID_EMAIL("invalid email"),
    INVALID_PHONE_NUMBER("invalid phone number"),
    USER_ALREADY_EXIST("user already exist"),
    EMAIL_ALREADY_EXIST("email already exist"),
    PHONE_NUMBER_ALREADY_EXIST("phone number already exist"),
    USER_IS_NOT_FOUND("user not found"),
    VALUE_IS_NULL("null is not allowed"),
    WRONG_PASSWORD("wrong password"),
    INVALID_DATA_VALUE("invalid value"),
    USER_IS_NOT_ENABLED("user not enabled"),
    RECOVERY_CODE_IS_EXPIRED("recovery code is expired"),
    INVALID_RECOVERY_CODE("invalid recovery code"),
    VERIFICATION_CODE_IS_EXPIRED("verification code is expired"),
    INVALID_VERIFIACTION_CODE("invalid verification code"),
    USER_ALREADY_ACTIVATED("user already activated"),
    UNHANDLED_EXCEPTION("UNHANDLED_EXCEPTION"),
    SEND_EMAIL_FAILED("send email falied"),
    NOT_FOUND_EXCEPTION("not found"),
    IO_EXCEPTION("IO_EXCEPTION"),
    ACCESS_FORBIDDEN("access denied"),
    TOKEN_EXPIRED("token expired"),
    ACTIVATION_CODE_NOT_VALID("code not valid"),
    NOT_AUTHORIZED("not authorized"),
    USERNAME_ALREADY_EXIST("username is taken"),
    NOT_ALLOWED("not allowed"),
    INVALID_TOKEN("invalid token"),
    REFRESH_TOKEN_NOT_VALID("refresh token not valid"),
    REFRESH_TOKEN_NOT_FOUND("refresh token not found"),
    USER_NOT_ACTIVATED("user not activated"),
    CODE_NOT_VALID("code not valid"),
    MESSAGE_NOT_SENT("message not sent");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}