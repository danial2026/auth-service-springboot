package com.springboot.server.authenticationservice.repository;

import com.springboot.server.authenticationservice.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByPhoneNumber(String username);

    Optional<UserEntity> findByUsernameOrPhoneNumber(String username, String phoneNumber);

    Optional<UserEntity> findByUsernameAndPhoneNumber(String username, String phoneNumber);

    List<UserEntity> findByUsernameIn(List<String> usernames);

    Boolean existsByUsername(String username);
}
