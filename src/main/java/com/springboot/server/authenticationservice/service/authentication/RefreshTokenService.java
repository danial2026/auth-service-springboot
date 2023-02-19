package com.springboot.server.authenticationservice.service.authentication;

import com.springboot.server.authenticationservice.entity.RefreshTokenEntity;
import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;
import com.springboot.server.authenticationservice.repository.RefreshTokenRepository;
import com.springboot.server.authenticationservice.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * RefreshTokenService
 *
 * @author: Danial
 */

@Service
public class RefreshTokenService {

    @Value("${security.refresh.expiration:#{24*60*60*270}}")
    private Long refreshTokenDurationMs;

    @Value("${security.jwt.expiration:#{24*60*60*90}}")
    private Long tokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;


    /**
     * check if a token for this user exist then remove it and save a refresh token entity with new values
     *
     * @param username
     * @param jwtToken
     * @return
     * @throws BusinessServiceException
     */
    public RefreshTokenEntity createRefreshToken(String username, String jwtToken) throws BusinessServiceException {
        String userId = userRepository.findByUsername(username).get().getId();
        RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity();
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUsername(username);

        if(refreshTokenEntity != null){
            try{
                refreshTokenRepository.deleteByUsername(username);
                refreshTokenRepository.deleteByUserId(userId);
                refreshTokenRepository.delete(refreshTokenEntity);
            }catch (Exception e){}
            newRefreshTokenEntity = new RefreshTokenEntity(refreshTokenEntity);
        }

        newRefreshTokenEntity.setUserId(userId);
        newRefreshTokenEntity.setUsername(username);

        newRefreshTokenEntity.setRefreshTokenExpiryDate(Instant.now().plusSeconds(refreshTokenDurationMs).toString());
        newRefreshTokenEntity.setRefreshTokenExpiryTimeInSeconds(refreshTokenDurationMs);
        newRefreshTokenEntity.setRefreshToken(UUID.randomUUID().toString());

        newRefreshTokenEntity.setTokenExpiryDate(Instant.now().plusSeconds(tokenDurationMs).toString());
        newRefreshTokenEntity.setToken(jwtToken);

        refreshTokenRepository.save(newRefreshTokenEntity);

        return newRefreshTokenEntity;
    }

    /**
     * check if a token for this user exist then remove it and save a refresh token entity with new values
     *
     * @param username
     * @param newToken
     * @param requestRefreshToken
     * @return
     * @throws BusinessServiceException
     */
    public RefreshTokenEntity updateRefreshToken(String username, String newToken, String requestRefreshToken) throws BusinessServiceException {
        String userId = userRepository.findByUsername(username).get().getId();
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(requestRefreshToken);
        RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity();

        if(refreshTokenEntity != null){
            try{
                refreshTokenRepository.deleteByUsername(username);
                refreshTokenRepository.deleteByUserId(userId);
                refreshTokenRepository.delete(refreshTokenEntity);
            }catch (Exception e){}
            newRefreshTokenEntity = new RefreshTokenEntity(refreshTokenEntity);
        }

        String newRefreshToken = UUID.randomUUID().toString();

        newRefreshTokenEntity.setUserId(userId);
        newRefreshTokenEntity.setUsername(username);

        newRefreshTokenEntity.setRefreshTokenExpiryDate(Instant.now().plusSeconds(refreshTokenDurationMs).toString());
        newRefreshTokenEntity.setRefreshTokenExpiryTimeInSeconds(refreshTokenDurationMs);
        newRefreshTokenEntity.setRefreshToken(newRefreshToken);

        newRefreshTokenEntity.setTokenExpiryDate(Instant.now().plusSeconds(refreshTokenDurationMs).toString());
        newRefreshTokenEntity.setToken(newToken);

        refreshTokenRepository.save(newRefreshTokenEntity);

        return newRefreshTokenEntity;
    }

    /**
     * validate the refresh token expiration
     *
     * @param token
     * @return RefreshTokenEntity
     * @throws BusinessServiceException
     */
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) throws BusinessServiceException {
        if (token.getRefreshTokenExpiryDate().compareTo(Instant.now().toString()) < 0) {
            refreshTokenRepository.delete(token);
            throw new AuthenticationServiceException(ErrorMessage.REFRESH_TOKEN_NOT_VALID);
        }

        return token;
    }

    public RefreshTokenEntity findByRefreshToken(String token) throws BusinessServiceException {

        return refreshTokenRepository.findByRefreshToken(token);
    }

    public void deleteByUserId(String userId) throws BusinessServiceException {

        refreshTokenRepository.deleteByUserId(userId);
    }

    public void deleteByRefreshToken(String refreshToken) throws BusinessServiceException {

        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
