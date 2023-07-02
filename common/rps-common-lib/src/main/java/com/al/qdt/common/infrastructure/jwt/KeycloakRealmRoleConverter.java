package com.al.qdt.common.infrastructure.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Keycloak jwt roles converter/
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS_NODE_NAME = "realm_access";
    private static final String ROLES_NODE_NAME = "roles";

    /**
     * Extract the roles from access token and returns an Authentication.
     * The roles are part of the claim realm_access.
     *
     * @param jwt jwt token
     * @return roles
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        final var realmAccess = (Map<String, Object>) jwt.getClaims().get(REALM_ACCESS_NODE_NAME);
        return ((List<String>) realmAccess.get(ROLES_NODE_NAME)).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
