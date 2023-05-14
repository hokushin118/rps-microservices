package com.al.qdt.common.infrastructure.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MicrometerConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    /**
     * Allowing Micrometer to add a timer to custom methods.
     * <p>
     * This is required so that we can use the @Timed annotation
     * on methods that we want to time.
     * See: https://micrometer.io/docs/concepts#_the_timed_annotation
     *
     * @param meterRegistry MeterRegistry
     * @return TimedAspect
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry meterRegistry) {
        return new TimedAspect(meterRegistry);
    }

    /**
     * Setting JVM thread metrics.
     *
     * @return JvmThreadMetrics
     */
    @Bean
    public JvmThreadMetrics threadMetrics() {
        return new JvmThreadMetrics();
    }
}
