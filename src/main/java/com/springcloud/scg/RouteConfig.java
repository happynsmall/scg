package com.springcloud.scg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
