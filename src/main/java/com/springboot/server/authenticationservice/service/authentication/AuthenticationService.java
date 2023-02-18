package com.springboot.server.authenticationservice.service.authentication;

import com.springboot.server.authenticationservice.entity.UserDetailsEntity;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;
import org.springframework.security.core.Authentication;


public interface AuthenticationService {

    /**
     * Get authenticated user
     *
     * @return UserDetailsEntity
     * @throws BusinessServiceException
     */
    UserDetailsEntity getAuthenticatedUser() throws BusinessServiceException;

    /**
     * Do user athentication
     *
     * @param username as String
     * @param password as String
     * @return
     * @throws BusinessServiceException
     */
    Authentication authenticate(String username, String password) throws BusinessServiceException;
}