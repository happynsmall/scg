package com.springcloud.scg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class PreLogger extends AbstractGatewayFilterFactory<PreLogger.Config> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public PreLogger() {
        super(Config.class);
    }

    @Override 
    public GatewayFilter apply(Config config) {
        //grab configuration from Config object 
        return (exchange, chain) -> {
            log.info(">>>>> Base Message => " + config.baseMessage);

            if(config.logging) log.info("============= START >>>>> "+exchange.getRequest());
            
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();

            return chain.filter(exchange.mutate().request(builder.build()).build());
        };
    }

    @Getter
    @Setter 
    public static class Config {
        private String baseMessage; 
        private boolean logging;
        
    }
}
