package com.springboot.server.authenticationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
/**
 * RefreshToken
 *
 * @author: Danial
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("refresh_token_entity")
public class RefreshTokenEntity implements Serializable {

    @Id
    @Indexed
    private Long id;

    @Indexed
    private String userId;

    @Indexed
    private String username;

    @Indexed
    private String refreshToken;

    private String refreshTokenExpiryDate;

    @TimeToLive
    private Long refreshTokenExpiryTimeInSeconds;

    @Indexed
    private String token;

    private String tokenExpiryDate;

    public RefreshTokenEntity(RefreshTokenEntity refreshTokenEntity) {
        this.userId = refreshTokenEntity.userId;
        this.username = refreshTokenEntity.username;
        this.refreshToken = refreshTokenEntity.refreshToken;
        this.refreshTokenExpiryTimeInSeconds = refreshTokenEntity.refreshTokenExpiryTimeInSeconds;
        this.token = refreshTokenEntity.token;
    }
}