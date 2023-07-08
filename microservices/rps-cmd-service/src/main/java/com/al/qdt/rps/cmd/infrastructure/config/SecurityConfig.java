package com.al.qdt.rps.cmd.infrastructure.config;

import com.al.qdt.common.infrastructure.jwt.KeycloakRealmRoleConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * REST API security configuration.
 */
@Configuration
@EnableWebSecurity
// https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html#jc-enable-method-security
@EnableGlobalMethodSecurity(
        // Required to grpc services security, https://github.com/yidongnan/grpc-spring-boot-starter/blob/master/docs/en/server/security.md
        proxyTargetClass = true,
        // Enables @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter annotations, these annotations support SpEL (Spring Expression Language)
        prePostEnabled = true,
        // Enables @Secured annotation, @Secured doesn’t support SpEL (Spring Expression Language) and is used to specify a list of roles on a method
        securedEnabled = true,
        // Enables @RolesAllowed annotation, https://en.wikipedia.org/wiki/Jakarta_Annotations
        jsr250Enabled = true)
class SecurityConfig {

    /**
     * OpenAPI v3.0 document path.
     */
    @Value("${springdoc.api-docs.path}")
    private String restApiDocPath;

    /**
     * OAuth 2.0 jwt set url.
     */
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    /**
     * Admin endpoint.
     */
    @Value("${api.endpoint-admin}")
    private String adminUrl;

    /**
     * Database endpoint.
     */
    @Value("${api.endpoint-db}")
    private String dbUrl;

    /**
     * Games endpoint.
     */
    @Value("${api.endpoint-games}")
    private String gamesUrl;

    /**
     * Creation and configuration of the {@link SecurityFilterChain} bean.
     *
     * @param http it allows configuring web based security for specific http requests
     * @return configured bean
     * @throws Exception
     * @see <a href="https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html">Authorization</a>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Enable anonymous
        http.anonymous();

        // Enable CORS
        http.cors();

        // Setting session management to stateless, no servlet sessions, client state is managed with URIs and access token
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Disable CSRF because of state-less session-management
        http.csrf().disable();

        http.authorizeHttpRequests()
                // Public endpoints
                // Actuator health and info probes, OpenAPI specs and UI should be accessible to anonymous
                .antMatchers("/actuator/health", "/actuator/info", "/actuator/prometheus")
                .permitAll()
                .antMatchers(String.format("%s/**", this.restApiDocPath))
                .permitAll()
                .antMatchers("/swagger-ui/**")
                .permitAll()
                // Private endpoints
                .antMatchers("/actuator/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, String.format("/%s/%s/**", this.adminUrl, this.dbUrl)).hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, String.format("/%s/%s/**", this.adminUrl, this.gamesUrl)).hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, String.format("/%s/%s/**", this.adminUrl, this.gamesUrl)).hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, String.format("/%s/**", this.gamesUrl)).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .anyRequest().authenticated()
                .and()
                // forcing Spring Boot to use the OAuth2 protocol for login, instead of the .loginForm() method
                // it takes precedence over any configuration property
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }

    private static Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        final var jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtConverter;
    }
}
