package com.al.qdt.score.qry.infrastructure.config;

import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.common.infrastructure.config.*;
import com.al.qdt.common.infrastructure.converters.StringToPlayerConverter;
import com.al.qdt.common.infrastructure.kafka.KafkaConsumerConfig;
import com.al.qdt.score.qry.domain.repositories.ScoreRepository;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

/**
 * Configuration of the Score query microservice.
 */
@Configuration
@Import({AppQryConfig.class,
        AsyncConfig.class,
        FilterConfig.class,
        MvcQryConfig.class,
        OpenApiConfig.class,
        KafkaConsumerConfig.class,
        ProtoConfig.class,
        ErrorHandlingConfig.class,
        MicrometerConfig.class
})
@EnableJpaRepositories(basePackageClasses = ScoreRepository.class)
@EnableTransactionManagement
public class AppConfig {

    /**
     * Creates jpa transaction manager.
     *
     * @param entityManagerFactory entity manager factory
     * @return jpa transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    /**
     * Adds Player enum converter to context.
     */
    @Bean
    public Converter<String, Player> stringToPlayerConverter() {
        return new StringToPlayerConverter();
    }

    /**
     * Modifies flyway migration strategy.
     *
     * @return flyway migration strategy
     */
    //    @Bean
    @Profile("dev")
    FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            flyway.clean(); // reset flyway history
            flyway.migrate();
        };
    }
}
