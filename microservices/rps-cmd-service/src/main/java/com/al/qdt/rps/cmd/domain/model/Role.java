package com.al.qdt.rps.cmd.domain.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role implements GrantedAuthority {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String ROLE_USER = "ROLE_USER";

    private String authority;
}
