package com.springboot.server.authenticationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * TokenRefreshRequest
 *
 * @author: Danial
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {

    @NotBlank
    private String refreshToken;

    @NotBlank
    private String accessToken;
}
