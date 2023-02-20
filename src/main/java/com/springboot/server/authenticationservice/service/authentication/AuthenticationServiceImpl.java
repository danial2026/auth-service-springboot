package com.springboot.server.authenticationservice.service.authentication;

import com.springboot.server.authenticationservice.entity.UserDetailsEntity;
import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetailsEntity getAuthenticatedUser() throws BusinessServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            UserDetailsEntity userDetailsEntity = (UserDetailsEntity) authentication.getPrincipal();
            return userDetailsEntity;
        }
        throw new AuthenticationServiceException(ErrorMessage.NOT_AUTHORIZED);
    }

    @Override
    public Authentication authenticate(final String username, final String password) throws BusinessServiceException {
        try {
            //TODO:Will add phone number that user could be able to login with phone number.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        } catch (Exception ex) {
            if (ex.getMessage().equals(ErrorMessage.USER_NOT_ACTIVATED.getMessage())){
                throw new AuthenticationServiceException(ErrorMessage.USER_NOT_ACTIVATED);
            }
            throw new AuthenticationServiceException(ErrorMessage.USER_IS_NOT_FOUND);
        }
    }
}