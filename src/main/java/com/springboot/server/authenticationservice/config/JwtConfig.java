package com.springboot.server.authenticationservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@Component
public class JwtConfig {

    @Value("${security.jwt.uri:/api/v1/users/**}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60*30}}")
    private Long expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;
}
