package com.al.qdt.common.infrastructure.config;

import com.google.protobuf.util.JsonFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Protobuf configuration.
 */
@Configuration(proxyBeanMethods = false)
public class ProtoConfig {

    /**
     * Converts proto messages to json.
     * Declare it as a primary http message converter.
     *
     * @return converter
     */
    @Bean
    @Primary
    public ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        final var parser = JsonFormat.parser()
                .ignoringUnknownFields();
        final var printer = JsonFormat.printer()
                .includingDefaultValueFields();
        return new ProtobufJsonFormatHttpMessageConverter(parser, printer);
    }

    /**
     * Creates RestTemplate.
     *
     * @param protobufHttpMessageConverter http message converter
     * @return configured RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(ProtobufHttpMessageConverter protobufHttpMessageConverter) {
        return new RestTemplate(List.of(protobufHttpMessageConverter));
    }
}
