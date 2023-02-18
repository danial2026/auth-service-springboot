package com.springboot.server.authenticationservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * AdminUserDTO
 *
 * @author: Danial
 */
@Data
public class UpgradeAuthorizationDTO {

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,16}$")
    private String adminUsername;

    @NotBlank
//    @Pattern(regexp = "^[A-Za-z]\\w{6,16}$")
    private String adminPassword;
}
