package com.al.qdt.common.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.al.qdt.common.infrastructure.helpers.Utils.createObjectMapper;

/**
 * ObjectMapper configuration.
 */
@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return createObjectMapper();
    }
}
