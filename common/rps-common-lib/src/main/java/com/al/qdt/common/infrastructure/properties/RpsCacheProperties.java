package com.al.qdt.common.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Caching properties.
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "app.caching")
public class RpsCacheProperties {
    private String ttlValue; // Time-To-Live (TTL) value
}
