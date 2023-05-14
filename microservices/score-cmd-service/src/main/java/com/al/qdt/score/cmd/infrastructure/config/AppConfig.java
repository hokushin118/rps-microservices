package com.al.qdt.score.cmd.infrastructure.config;

import com.al.qdt.common.infrastructure.config.*;
import com.al.qdt.common.infrastructure.kafka.KafkaProducerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration of the Score command microservice.
 */
@Configuration
@Import({AppCmdConfig.class,
        AsyncConfig.class,
        FilterConfig.class,
        MvcCmdConfig.class,
        OpenApiConfig.class,
        MongoConfig.class,
        KafkaProducerConfig.class,
        ProtoConfig.class,
        ErrorHandlingConfig.class,
        MicrometerConfig.class
})
public class AppConfig {
}
