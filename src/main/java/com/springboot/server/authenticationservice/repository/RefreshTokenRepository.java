package com.springboot.server.authenticationservice.repository;

import com.springboot.server.authenticationservice.entity.RefreshTokenEntity;
import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RefreshTokenRepository
 *
 * @author: Danial
 */

@Repository
public class RefreshTokenRepository {

    @Autowired
    private RefreshTokenCrudRepository refreshTokenCrudRepository;

    public void save(RefreshTokenEntity refreshTokenEntity) throws BusinessServiceException {

        refreshTokenCrudRepository.save(refreshTokenEntity);
    }

    public RefreshTokenEntity findByUsername(String username) throws BusinessServiceException {
        RefreshTokenEntity refreshTokenEntity =  refreshTokenCrudRepository.findByUsername(username);

        return  refreshTokenEntity;
    }

    public RefreshTokenEntity findByUserId(String userId) throws BusinessServiceException {
        RefreshTokenEntity refreshTokenEntity =  refreshTokenCrudRepository.findByUserId(userId);

        return  refreshTokenEntity;
    }

    public RefreshTokenEntity findByRefreshToken(String refreshToken) throws BusinessServiceException {
        Optional<RefreshTokenEntity> refreshTokenEntity =  refreshTokenCrudRepository.findByRefreshToken(refreshToken);
        if (refreshTokenEntity.isEmpty()) {
            throw new RepositoryException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND);
        }
        return refreshTokenEntity.get();
    }

    // returns the count number of refresh_token_entity found in redis
    public long getSize() throws BusinessServiceException {
        long size;
        try{
            size = refreshTokenCrudRepository.count();
//            size = refreshTokenTemplate.opsForHash().size("refresh_token_entity");
        }catch (Exception e){
            size = 0;
        }

        return size;
    }

    public void deleteByUserId(String userId) throws BusinessServiceException {
        RefreshTokenEntity refreshTokenEntity = refreshTokenCrudRepository.findByUserId(userId);
        if (refreshTokenEntity == null) {
            throw new RepositoryException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND);
        }
        delete(refreshTokenEntity);
    }

    public void deleteByUsername(String username) throws BusinessServiceException {
        RefreshTokenEntity refreshTokenEntity = refreshTokenCrudRepository.findByUsername(username);
        if (refreshTokenEntity == null) {
            throw new RepositoryException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND);
        }
        delete(refreshTokenEntity);
    }

    public void deleteByRefreshToken(String refreshToken) throws BusinessServiceException {
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenCrudRepository.findByRefreshToken(refreshToken);
        if (refreshTokenEntity.isEmpty()) {
            throw new RepositoryException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND);
        }
        delete(refreshTokenEntity.get());
    }

    public void delete(RefreshTokenEntity refreshTokenEntity) throws BusinessServiceException {

        refreshTokenCrudRepository.delete(refreshTokenEntity);
    }
}
