package com.yl.gateway.filter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.yl.common.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class MyLogFilter2 implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("*****自定义过滤器：时间:" + DateUtil.formatDateTime(new Date()));
        log.info("*****自定义过滤器：请求:" + JSONUtil.toJsonStr(exchange.getRequest()));
        exchange.getRequest().mutate().header(AppConstant.TRACE_ID, UUID.randomUUID().toString()).build();
        //该条过滤的ServerWebExchange 对象传入到过滤链中的下一个过滤环节
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public String getTraceId(ServerWebExchange exchange) {
        String traceId = null;
        try {
            Object entrySpanInstance = exchange.getAttributes().get("SKYWALING_SPAN");

            Class<?> entrySpanClazz = entrySpanInstance.getClass().getSuperclass().getSuperclass();

            Field field = entrySpanClazz.getDeclaredField("owner");

            field.setAccessible(true);

            Object ownerInstance = field.get(entrySpanInstance);

            Class<?> ownerClazz = ownerInstance.getClass();

            Method getTraceId = ownerClazz.getMethod("getReadablePrimaryTraceId");

            traceId = (String) getTraceId.invoke(ownerInstance);
            return traceId;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
