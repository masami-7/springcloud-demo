package com.yl.gateway.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CBConfig {

//    @Resource
//    CircuitBreaker circuitBreaker;
//
//    @PostConstruct
//    public void init() {
//        circuitBreaker.getEventPublisher()
//                .onEvent(event -> System.out.println(JSONUtil.toJsonStr(event)));
//    }

//    @Bean
//    public ReactiveResilience4JCircuitBreakerFactory defaultCustomizer() {
//        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom() //
////                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED) // 滑动窗口的类型为时间窗口
//                .slidingWindowSize(10) // 时间窗口的大小为10秒
//                .minimumNumberOfCalls(5) // 在单位时间窗口内最少需要5次调用才能开始进行统计计算
//                .failureRateThreshold(30) // 在单位时间窗口内调用失败率达到50%后会启动断路器
//                .enableAutomaticTransitionFromOpenToHalfOpen() // 允许断路器自动由打开状态转换为半开状态
//                .permittedNumberOfCallsInHalfOpenState(5) // 在半开状态下允许进行正常调用的次数
//                .waitDurationInOpenState(Duration.ofSeconds(5)) // 断路器打开状态转换为半开状态需要等待5秒
//                .recordExceptions(Exception.class, BusinessException.class) // 所有异常都当作失败来处理
//                .build();
//
//        ReactiveResilience4JCircuitBreakerFactory factory = new ReactiveResilience4JCircuitBreakerFactory();
//        factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6)).build())
//                .circuitBreakerConfig(circuitBreakerConfig).build());
////                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()).build());
//
//        return factory;
//    }

}
