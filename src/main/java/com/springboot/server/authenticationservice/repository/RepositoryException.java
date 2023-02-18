package com.springboot.server.authenticationservice.repository;

import com.springboot.server.authenticationservice.enums.ErrorMessage;
import com.springboot.server.authenticationservice.exception.BusinessServiceException;

/**
 * RepositoryException
 *
 * @author: Danial
 */

public class RepositoryException extends BusinessServiceException {

    public RepositoryException(ErrorMessage ErrorMessage) {
        super(ErrorMessage);
    }
}