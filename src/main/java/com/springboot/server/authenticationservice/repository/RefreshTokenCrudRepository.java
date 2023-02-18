package com.springboot.server.authenticationservice.repository;

import com.springboot.server.authenticationservice.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * RefreshTokenCrudRepository
 *
 * @author: Danial
 */

public interface RefreshTokenCrudRepository extends CrudRepository<RefreshTokenEntity, String> {

    RefreshTokenEntity findByUserId(String userId);

    RefreshTokenEntity findByUsername(String username);

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

}
