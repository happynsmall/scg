package com.springcloud.scg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Component
public class PostLogger extends AbstractGatewayFilterFactory<PostLogger.Config> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public PostLogger() {
        super(Config.class);
    }

    @Override 
    public GatewayFilter apply(Config config) {
        //grab configuration from Config object 
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable( () -> {
                log.info(">>>>> PostGatewayFilterFactory Message => " + config.baseMessage);

                ServerHttpResponse response = exchange.getResponse(); 
                if(config.logging) log.info("============= END >>>>> "+exchange.getResponse());
            }));
        };
    }

    @Getter 
    @Setter
    public static class Config {
        private String baseMessage; 
        private boolean logging;        
    }
}
