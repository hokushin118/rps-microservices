package com.al.qdt.common.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static com.al.qdt.common.infrastructure.helpers.Constants.MAX_AGE_SECS;

@Configuration
public class MvcQryConfig implements WebMvcConfigurer {

    @Bean
    public ViewResolver getViewResolver() {
        var resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET")
                .maxAge(MAX_AGE_SECS);
    }
}
