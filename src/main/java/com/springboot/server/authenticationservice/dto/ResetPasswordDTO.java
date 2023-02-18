package com.springboot.server.authenticationservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordDTO extends BaseDTO {

    @NotBlank
    private String userIdentity;

    @NotBlank
    private String recoveryCode;

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,16}$")
    private String newPassword;
}