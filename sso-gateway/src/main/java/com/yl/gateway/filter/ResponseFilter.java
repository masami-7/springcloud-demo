//package com.yl.gateway.filter;
//
//import cn.hutool.json.JSONUtil;
//import com.yl.common.config.Result;
//import com.yl.common.config.ResultCode;
//import lombok.extern.slf4j.Slf4j;
//import org.reactivestreams.Publisher;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//
//import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;
//
//@Component
//@Slf4j
//public class ResponseFilter implements GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpResponse original = exchange.getResponse();
//
//        // 创建新的response装饰对象，并传入原始response对象，然后重写writeWith方法
//        ServerHttpResponseDecorator newResponse = new ServerHttpResponseDecorator(original) {
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//                if (body instanceof Flux) {
//                    Mono<DataBuffer> mono = DataBufferUtils.join(body).map(buffer -> {
//                        try {
//                            String data = buffer.toString(StandardCharsets.UTF_8);
//                            // 修改返回内容
//                            String mutatedData = mutate(data);
//                            byte[] bytes = mutatedData.getBytes(StandardCharsets.UTF_8);
//                            // 修改了返回数据之后，需要重新设置Content-Length头，否则可能造成数据传输不完整的情况
//                            getHeaders().setContentLength(bytes.length);
//                            // 获取buffer工厂，使用新的数据重新生成ByteBuffer对象
//                            return this.bufferFactory().wrap(bytes);
//                        } finally {
//                            // 对于自己创建的ByteBuffer对象，在使用完成之后，需要手动release，否则会造成内存泄露
//                            DataBufferUtils.release(buffer);
//                        }
//                    });
//                    return super.writeWith(mono);
//                }
//
//                return super.writeWith(body);
//            }
//        };
//        return chain.filter(exchange.mutate().response(newResponse).build());
//    }
//
//    private String mutate(String data) {
//        System.out.println(data);
//        return JSONUtil.toJsonStr(Result.success(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), JSONUtil.parseObj(data)));
//    }
//
//    @Override
//    public int getOrder() {
//        return WRITE_RESPONSE_FILTER_ORDER - 1;
//    }
//
//}
