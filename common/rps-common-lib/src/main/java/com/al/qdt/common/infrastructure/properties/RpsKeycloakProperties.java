package com.al.qdt.common.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Keycloak properties.
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "keycloak")
public class RpsKeycloakProperties {
    private String schema; // schema
    private String hostname; // hostname
    private int port; // port
    private String realm; // realm
    private String clientId; // client-id
    private String secret; // secret
}
