package com.al.qdt.score.qry.infrastructure.config;

import com.al.qdt.rps.grpc.v1.services.ScoreQryServiceGrpc;
import net.devh.boot.grpc.server.security.check.AccessPredicate;
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource;
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * gRPC API security configuration.
 */
@Configuration(proxyBeanMethods = false)
public class GrpcSecurityConfig {

    @Bean
    public GrpcSecurityMetadataSource grpcSecurityMetadataSource() {
        final var source = new ManualGrpcSecurityMetadataSource();
        source.set(ScoreQryServiceGrpc.getListOfScoresMethod(), AccessPredicate.hasAllRoles("ROLE_USER", "ROLE_ADMIN"));
        source.set(ScoreQryServiceGrpc.getFindByIdMethod(), AccessPredicate.hasAllRoles("ROLE_USER", "ROLE_ADMIN"));
        source.set(ScoreQryServiceGrpc.getFindByWinnerMethod(), AccessPredicate.hasAllRoles("ROLE_USER", "ROLE_ADMIN"));
        source.setDefault(AccessPredicate.denyAll());
        return source;
    }
}
