package com.springcloud.scg;

import java.time.Duration;
import java.util.function.Function;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class RouteConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public GlobalFilter filter1() {
        return new CustomGlobalFilter();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        // 순서 : order -> predicates -> filters -> uri -> metadata -> id
        return builder.routes()
            // /spring-images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg
            
            .route(r -> 
                r.order(0)
                .path("/spring-images/**")
                .filters(f -> 
                    f.addRequestHeader("x-header1", "springcloud")
                    .rewritePath("/.*/(?<image>.*)", "/images/${image}")
                )
                .uri("https://spring.io")
                .metadata("response-timeout", 1000)
                .metadata("connect-timeout", 1000)
                .id("images")
            )

            // /daum-images/20200723055344399.png
            .route(r -> 
                r.order(-1)
                .path("/daum-images/**")
                .filters(f -> f.rewritePath("/.*/(?<image>.*)", "/daumtop_chanel/op/${image}"))
                .uri("https://t1.daumcdn.net")

            ).build();
    }

   
/*    
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> slowCustomizer() {
        return factory -> 
            factory.configure(
                builder -> 
                    builder.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(
                        TimeLimiterConfig
                        .custom()
                        .timeoutDuration(Duration.ofSeconds(2))
                        .build()
                    ), "slow");
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> slowCustomizerEx() {
        return factory -> 
            factory.addCircuitBreakerCustomizer(
                circuitBreaker -> 
                    circuitBreaker.getEventPublisher()
                    .onError(normalFluxErrorConsumer)
                    .onSuccess(normalFluxSuccessConsumer), "normalflux");
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> myCustomizer() {
        return factory -> {
            factory.configure(
                builder -> 
                    builder.timeLimiterConfig(
                        TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(2))
                        .build()
                    )
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                , "slow", "slowflux");
                
                factory
                .addCircuitBreakerCustomizer(circuitBreaker -> circuitBreaker.getEventPublisher()
                .onError(normalFluxErrorConsumer)
                .onSuccess(normalFluxSuccessConsumer), "normalflux");
         };
    }

*/
/*
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> 
            factory.configureDefault(
                builder -> 
                    new Resilience4JConfigBuilder(builder)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(
                        TimeLimiterConfig
                        .custom()
                        .timeoutDuration(Duration.ofSeconds(3))
                        .build()
                    )
                    .build()
                );
    }


    @Bean
    public ReactiveCircuitBreakerFactory circuitBreakerFactory() {
        final ReactiveResilience4JCircuitBreakerFactory factory = new ReactiveResilience4JCircuitBreakerFactory();
        
        factory.configureDefault(new Function<String, Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration>() {
            @Override
            public Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration apply(String s) {
                return new Resilience4JConfigBuilder(s)
                        .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(5)).build())
                        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                        .build();
            }
        });
        return factory;
    }
*/
    
}
