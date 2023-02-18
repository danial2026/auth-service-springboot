package com.springboot.server.authenticationservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ActivationCodeDTO extends BaseDTO {

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,16}$")
    private String username;

    @NotBlank
//    @Pattern(regexp = "^[0-9]{4,7}$")
    private String code;
}
