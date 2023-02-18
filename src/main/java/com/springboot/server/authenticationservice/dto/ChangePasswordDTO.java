package com.springboot.server.authenticationservice.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ChangePasswordDTO extends BaseDTO {

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,16}$")
    private String newPassword;

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,16}$")
    private String currentPassword;
}