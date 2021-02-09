package com.springcloud.scg;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

//ref: https://github.com/spring-cloud/spring-cloud-gateway/issues/608#issuecomment-431852899

@Component 
public class UriHostPlaceholderFilter extends AbstractGatewayFilterFactory<UriHostPlaceholderFilter.Config> {
    private final Logger log = LoggerFactory.getLogger(getClass()); 
    
    public UriHostPlaceholderFilter() {
        super(Config.class);
    }

    @Override 
    public GatewayFilter apply(Config config) {
        //log.info(">>>>>>>>>>> order: "+config.order);

        return new OrderedGatewayFilter((exchange, chain) -> {
            String serivceID = "";
            String downStreamPath ="";
            URI uri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
            
            LinkedHashSet<URI> originalURI = exchange
                    .getRequiredAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);			
            addOriginalRequestUrl(exchange,  uri);					
            String path = originalURI.stream().findFirst().get().getPath();
            Pattern pattern = Pattern.compile("/(?<eurekaSerivceId>[^/]*)/(?<downStreamPath>.*)");
            if (path != null) { 
                Matcher matcher = pattern.matcher(path);
                boolean flag = matcher.matches();
                serivceID = matcher.group("eurekaSerivceId");
                downStreamPath = matcher.group("downStreamPath");
            }
            String newurl = "lb://"+serivceID.toUpperCase()+"/"+downStreamPath;
            log.info(String.format("****** URI: %s => %s", path, newurl));

            URI newUri =null;
            try {
                newUri = new URI(newurl);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }		
            
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newUri);			
            return chain.filter(exchange);
        },config.order);
    }

    @Getter
    @Setter 
    public static class Config {
        private int order;
    
    }
}
