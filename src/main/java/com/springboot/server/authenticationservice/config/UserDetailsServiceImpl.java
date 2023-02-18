package com.springboot.server.authenticationservice.config;

import com.springboot.server.authenticationservice.entity.UserDetailsEntity;
import com.springboot.server.authenticationservice.entity.UserEntity;
import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.repository.UserRepository;
import com.springboot.server.authenticationservice.exception.UserNotEnabledException;
import com.springboot.server.authenticationservice.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userService;

    public UserDetailsServiceImpl(UserRepository userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userService
                    .findByUsername(username)
                    .filter(this::isUserActivated)
                    .map(UserDetailsEntity::new)
                    .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_IS_NOT_FOUND.getMessage()));
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param user
     * @return true if user is activated.
     */
    private boolean isUserActivated(UserEntity user) throws UserNotEnabledException {
        if (user.isActivated()) {
            return true;
        }

        throw new UserNotEnabledException(ErrorMessage.USER_NOT_ACTIVATED.getMessage());
    }

}

