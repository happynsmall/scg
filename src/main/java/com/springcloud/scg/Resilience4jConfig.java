package com.springcloud.scg;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;

@Configuration
@RefreshScope
public class Resilience4jConfig {
    @Value("${resilience4j.circuitbreaker.default.slidingWindowType:COUNT_BASED}")
    private String slidingWindowType;

    @Value("${resilience4j.circuitbreaker.default.slidingWindowSize:10}")
    private int slidingWindowSize;

    @Value("${resilience4j.circuitbreaker.default.minimumNumberOfCalls:5}")
    private int minimumNumberOfCalls;

    @Value("${resilience4j.circuitbreaker.default.failureRateThreshold:50}")
    private float failureRateThreshold;

    @Value("${resilience4j.circuitbreaker.default.waitDurationInOpenState:5000}")
    private long waitDurationInOpenState;

    @Value("${resilience4j.circuitbreaker.default.permittedNumberOfCallsInHalfOpenState:2}")
    private int permittedNumberOfCallsInHalfOpenState;

    @Value("${resilience4j.circuitbreaker.default.slowCallDurationThreshold:3000}")
    private long slowCallDurationThreshold;

    @Value("${resilience4j.circuitbreaker.default.slowCallRateThreshold:100}")
    private float slowCallRateThreshold;

    
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {

        SlidingWindowType winType = ("COUNT_BASED".equals(this.slidingWindowType)?SlidingWindowType.COUNT_BASED:SlidingWindowType.TIME_BASED);

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .slidingWindowType(winType)
            .slidingWindowSize(this.slidingWindowSize)
            .minimumNumberOfCalls(this.minimumNumberOfCalls)
            .failureRateThreshold(this.failureRateThreshold)
            .waitDurationInOpenState(Duration.ofMillis(this.waitDurationInOpenState))
            .permittedNumberOfCallsInHalfOpenState(this.permittedNumberOfCallsInHalfOpenState)
            .slowCallDurationThreshold(Duration.ofMillis(this.slowCallDurationThreshold))
            .slowCallRateThreshold(this.slowCallRateThreshold)
            .build();

        return factory -> 
            factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build()
            );
    }

    @Value("${resilience4j.circuitbreaker.mycb.minimumNumberOfCalls:5}")
    private int myCBminimumNumberOfCalls;

    @Value("${resilience4j.circuitbreaker.mycb.failureRateThreshold:50}")
    private float myCBfailureRateThreshold;

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> myCustomizer() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .minimumNumberOfCalls(this.myCBminimumNumberOfCalls)
            .failureRateThreshold(this.myCBfailureRateThreshold)
            .build();

        return factory ->
            factory.configure(builder -> builder.circuitBreakerConfig(config)
            .build(), "mycb");
    }
}