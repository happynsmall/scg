package com.springcloud.scg;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

@Configuration
public class Resilience4jConfig {
    @Bean 
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                 .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                 .build());
    }

    // @Bean
    // public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
    //     CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
    //             .failureRateThreshold(5)
    //             .permittedNumberOfCallsInHalfOpenState(2)
    //             .slidingWindowSize(2)
    //             .minimumNumberOfCalls(5)
    //             .waitDurationInOpenState(Duration.ofMillis(20000))
    //             .build();
    //     return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
    //             .circuitBreakerConfig(circuitBreakerConfig)
    //             .build());
    // }    

    // @Bean 
    // public Customizer<ReactiveResilience4JCircuitBreakerFactory> myCustomizer() {
    //     CircuitBreakerConfig config = CircuitBreakerConfig.custom()
    //     .minimumNumberOfCalls(3)
    //     .waitDurationInOpenState(Duration.ofMillis(5000))
    //     .build(); 

    //     return factory -> 
    //         factory.configure(builder -> builder.circuitBreakerConfig(config)
    //         .build(), "mycb");
    // }
}