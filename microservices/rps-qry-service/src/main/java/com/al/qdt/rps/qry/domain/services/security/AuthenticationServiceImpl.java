package com.al.qdt.rps.qry.domain.services.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UUID getUserId() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(authentication.getName());
    }
}
