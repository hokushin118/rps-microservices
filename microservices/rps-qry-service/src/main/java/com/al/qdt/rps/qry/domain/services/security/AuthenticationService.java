package com.al.qdt.rps.qry.domain.services.security;

import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface AuthenticationService {

    /**
     * Obtaining authentication context.
     * @return authentication context
     */
    Authentication getAuthentication();

    /**
     * Obtaining user id from authentication context.
     * @return user id
     */
    UUID getUserId();
}
