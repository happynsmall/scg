package com.springcloud.scg;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class Resilience4jConfig {
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
        CircuitBreakerConfig circuitBreakerConfig = 
            CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .slidingWindowSize(2)
            .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3)).build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig).circuitBreakerConfig(circuitBreakerConfig).build());
    }    

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfiguration2() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().build();
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(1)).build();

        return factory -> 
            factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig)
            .timeLimiterConfig(timeLimiterConfig).build(),
            "mycb", "mycb2", "mycb3");
    }

}
