package com.springboot.server.authenticationservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpDTO extends BaseDTO {

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{3,40}$")
    private String fullName;

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,16}$")
    private String username;

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,20}$")
    private String password;

    @NotBlank
//    @Pattern(regexp = "^[0-9]{11,13}$")
    private String phoneNumber;
}
