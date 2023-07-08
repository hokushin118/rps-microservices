package com.al.qdt.common.infrastructure.config;

import com.al.qdt.common.infrastructure.properties.RpsApiProperties;
import com.al.qdt.common.infrastructure.properties.RpsKeycloakProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Open API 3.0 configuration.
 */
@Configuration
@EnableConfigurationProperties({RpsApiProperties.class, RpsKeycloakProperties.class})
@RequiredArgsConstructor
public class OpenApiConfig {
    private static final String OAUTH_SCHEME_NAME = "keycloak_oauth2_security_schema";

    private final BuildProperties buildProperties;
    private final RpsApiProperties rpsApiProperties;
    private final RpsKeycloakProperties rpsKeycloakProperties;

    @Bean
    public OpenAPI rpsOpenApi() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME_NAME, new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("Authentication needed for this operation")
                                .flows(new OAuthFlows()
                                        .implicit(createAuthorizationCodeFlow()))))
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME))
                .info(new Info()
                        .title(this.buildProperties.get("app.name"))
                        .version(this.buildProperties.get("app.version"))
                        .description(this.buildProperties.get("app.description"))
                        .license(new License().name(this.rpsApiProperties.getLicenseName())
                                .url(this.rpsApiProperties.getLicenseUrl()))
                        .contact(new Contact()
                                .name("RPS")
                                .url("http://rps.internal")
                                .email("info@rps.internal")))
                .servers(List.of(new Server().url(this.rpsApiProperties.getDev().getServer().getBaseUrl())
                                .description(this.rpsApiProperties.getDev().getServer().getDescription()),
                        new Server().url(this.rpsApiProperties.getProd().getServer().getBaseUrl())
                                .description(this.rpsApiProperties.getProd().getServer().getDescription())));
    }

    private OAuthFlow createAuthorizationCodeFlow() {
        final var authUrl = String.format("%s://%s:%s/realms/%s/protocol/openid-connect",
                this.rpsKeycloakProperties.getSchema(),
                this.rpsApiProperties.getKcHostname(),
                this.rpsKeycloakProperties.getPort(),
                this.rpsKeycloakProperties.getRealm());
        return new OAuthFlow()
                .authorizationUrl(authUrl + "/auth")
                .refreshUrl(authUrl + "/token")
                .tokenUrl(authUrl + "/token")
                .scopes(new Scopes()
                        .addString("openid", "openid")
                        .addString("profile", "profile"));
    }
}
