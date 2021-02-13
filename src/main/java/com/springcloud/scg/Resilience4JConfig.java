package com.springcloud.scg;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Configuration
public class Resilience4JConfig {

    @Bean
    public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(CircuitBreakerRegistry circuitBreakerRegistry) {
        ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory = new ReactiveResilience4JCircuitBreakerFactory();
        reactiveResilience4JCircuitBreakerFactory.configureCircuitBreakerRegistry(circuitBreakerRegistry);

        reactiveResilience4JCircuitBreakerFactory.configure(
                builder -> builder
                        //.timeLimiterConfig(timeLimiterRegistry.getConfiguration("mycb").orElse(TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(1000)).build()))
                        .circuitBreakerConfig(
                            circuitBreakerRegistry
                            .getConfiguration("mycb")
                            .orElse(circuitBreakerRegistry.getDefaultConfig())
                        )
                ,
                "mycb");

        return reactiveResilience4JCircuitBreakerFactory;
    }
}

