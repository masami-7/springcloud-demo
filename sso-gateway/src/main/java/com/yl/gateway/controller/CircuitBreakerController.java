package com.yl.gateway.controller;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CircuitBreakerController {

    @GetMapping("/fallback")
    public Mono<String> fallback() {
        System.out.println("fallback------------------");
        return Mono.just("fallback------------------");
    }

    private Mono<String> fallback(int param1, CallNotPermittedException e) {
        return Mono.just("Handled the exception when the CircuitBreaker is open");
    }

    private Mono<String> fallback(int param1, BulkheadFullException e) {
        return Mono.just("Handled the exception when the Bulkhead is full");
    }

    private Mono<String> fallback(int param1, NumberFormatException e) {
        return Mono.just("Handled the NumberFormatException");
    }

    private Mono<String> fallback(int param1, Exception e) {
        return Mono.just("Handled any other exception");
    }

    @GetMapping("/exception/{num}")
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    public Mono<String> exception(@PathVariable int num) {
        if (num == 1) {
            System.out.println("-----------------");
            return Mono.empty();
        } else {
            return Mono.error(new NumberFormatException());
        }
    }

    @GetMapping("/sleep/{num}")
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    public Mono<String> sleep(@PathVariable int num) {
        try {
            Thread.sleep(num * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Mono.empty();
    }

}
