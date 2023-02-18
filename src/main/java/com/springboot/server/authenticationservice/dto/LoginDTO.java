package com.springboot.server.authenticationservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginDTO extends BaseDTO {

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,16}$")
    private String username;

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,16}$")
    private String password;
}
