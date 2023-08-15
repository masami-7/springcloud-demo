package com.yl.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component("defaultKeyResolver")
public class DefaultKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        String key = "keyResolver-" + exchange.getRequest().getURI().getPath();
        System.out.println(key);
        return Mono.just(key);
    }

}
