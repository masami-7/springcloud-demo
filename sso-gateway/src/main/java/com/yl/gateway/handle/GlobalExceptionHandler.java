//package com.yl.gateway.handle;
//
//import cn.hutool.json.JSONUtil;
//import com.yl.common.config.Result;
//import com.yl.common.config.ResultCode;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//
///**
// * 网关异常通用处理器
// */
//@Component
//@Order(-1)
//@Slf4j
//public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
//
//    @Override
//    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
//        log.error("网关错误,请重试: {}", ex.getMessage());
//        //获取响应对象
//        ServerHttpResponse response = exchange.getResponse();
//
//        //已经commit，则直接返回异常
//        if (response.isCommitted()) {
//            return Mono.error(ex);
//        }
//
//        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//        if (ex instanceof ResponseStatusException) {
//            response.setStatusCode(((ResponseStatusException) ex).getStatus());
//        }
//
//        return response
//                .writeWith(Mono.fromSupplier(() -> {
//                    DataBufferFactory bufferFactory = response.bufferFactory();
//                    try {
//                        String data = mutate(ex.getMessage());
//                        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
//                        return bufferFactory.wrap(bytes);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return bufferFactory.wrap(new byte[0]);
//                    }
//                }));
//    }
//
//    private String mutate(String data) {
//        return JSONUtil.toJsonStr(Result.success(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data));
//    }
//
//}
