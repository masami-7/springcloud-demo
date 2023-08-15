package com.yl.gateway;

import cn.hutool.core.util.RandomUtil;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.vavr.control.Try;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
public class CircuitbreakerTest {

    // 测试的总次数
    private static int i = 0;

    @Autowired
    private WebTestClient webClient;

    @Test
    @RepeatedTest(50)
    void testHelloPredicates() throws InterruptedException {
        // 发起web请求
        webClient.get()
                .uri("/exception/" + RandomUtil.randomInt(2))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(String.class).consumeWith(result -> System.out.println(result.getRawStatusCode() + " - " + result.getResponseBody()));

        Thread.sleep(100);
    }

    public static void main(String[] args) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .ringBufferSizeInClosedState(2)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .build();
        CircuitBreaker circuitBreaker = CircuitBreaker.of("testName", circuitBreakerConfig);
        circuitBreaker.onError(0, TimeUnit.SECONDS, new RuntimeException());
        System.out.println(circuitBreaker.getState());
        circuitBreaker.onError(0, TimeUnit.SECONDS, new RuntimeException());
        System.out.println(circuitBreaker.getState());
        Try<String> result = Try.of(CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> "Hello"))
                .map(value -> value + " world");
        System.out.println(result.isSuccess());
        System.out.println(result.get());
    }

}
