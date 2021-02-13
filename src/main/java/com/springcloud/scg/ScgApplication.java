package com.springcloud.scg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class ScgApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScgApplication.class, args);
	}

    @Bean
    public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(CircuitBreakerRegistry circuitBreakerRegistry) {
        ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory = new ReactiveResilience4JCircuitBreakerFactory();
        reactiveResilience4JCircuitBreakerFactory.configureCircuitBreakerRegistry(circuitBreakerRegistry);

        reactiveResilience4JCircuitBreakerFactory.configure(
				builder -> builder
						//.timeLimiterConfig(timeLimiterRegistry.getConfiguration("backendB").orElse(TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(300)).build()))
						.circuitBreakerConfig(
                            circuitBreakerRegistry
                            .getConfiguration("myCB")
                            .orElse(circuitBreakerRegistry.getDefaultConfig())
                        )
                ,
				"myCB");

        return reactiveResilience4JCircuitBreakerFactory;
    }	
}
