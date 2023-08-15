//package com.yl.gateway.filter;
//
//import com.alibaba.nacos.common.utils.StringUtils;
//import com.yl.common.util.JWTUtil;
//import com.yl.gateway.config.CustomProperties;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpCookie;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.util.PathMatcher;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URI;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.util.List;
//
//@Component
//@Slf4j
//public class AccessFilter implements GlobalFilter, Ordered {
//
//    /**
//     * 登录服务地址
//     */
//    public static final String LOGIN_PATH = "/user/loginPage?login_callback=";
//    /**
//     * 登录成功后的回调接口地址
//     */
//    public static final String LOGIN_CALLBACK_PATH = "/login/callback";
//    /**
//     * 登录成功后的token名称
//     */
//    public static final String TOKEN_NAME = "z-token";
//
//    /**
//     * 路由匹配器
//     */
//    public static PathMatcher pathMatcher = new AntPathMatcher();
//
//    @Autowired
//    private CustomProperties customProperties;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        try {
//            // 如果未启用网关验证，则跳过
//            if (!customProperties.getEnable()) {
//                return chain.filter(exchange);
//            }
//
//            ServerHttpRequest request = exchange.getRequest();
//            String path = request.getPath().toString();
//            // 登录接口等，跳过
//            if (isMatch(customProperties.getIgnoreUrl(), path)) {
//                return chain.filter(exchange);
//            }
//
//            if (LOGIN_CALLBACK_PATH.equals(path)) {
//                String token = request.getQueryParams().getFirst(TOKEN_NAME);
//                String url = request.getQueryParams().getFirst("url");
//                if (StringUtils.isAnyBlank(token, url)) {
//                    throw new RuntimeException("参数错误!");
//                }
//                String real_url = URLDecoder.decode(url, "UTF-8");
//                boolean validateLoginToken = JWTUtil.verifyToken(token);
//                if (validateLoginToken) {
//                    ServerHttpResponse response = exchange.getResponse();
//                    response.setStatusCode(HttpStatus.SEE_OTHER);// 校验成功，重定向
//                    response.getHeaders().set(HttpHeaders.LOCATION, real_url);
//                    return exchange.getResponse().setComplete();
//                }
//                // 校验失败，抛异常或者重新执行登录
//                // redirectSSO(exchange, request, url);
//                throw new RuntimeException("token校验失败!");
//            }
//            String loginToken = getLoginToken(exchange);
//            Mono<Void> result = null;
//            if (StringUtils.isNoneBlank(loginToken)) {
//                boolean hasLogin = JWTUtil.verifyToken(loginToken);
//                if (!hasLogin) {
//                    result = redirectSSO(exchange, request);
//                } else {
//                    return chain.filter(exchange);
//                }
//            } else {
//                result = redirectSSO(exchange, request);
//            }
//            if (null != result) {
//                return result;
//            }
//            throw new RuntimeException("token校验失败!");
//        } catch (Exception e) {
//            exchange.getResponse().getHeaders().set("Content-Type", "application/json; charset=utf-8");
//            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(e.getMessage().getBytes())));
//        }
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//
//    /**
//     * 调单点登录
//     *
//     * @param exchange 上下文
//     * @param request  请求
//     * @return
//     */
//    private Mono<Void> redirectSSO(ServerWebExchange exchange, ServerHttpRequest request) {
//        URI uri = request.getURI();
//        String url = "/";
//        try {
//            // 登录服务地址
//            url = LOGIN_PATH
//                    // gateway服务（http://gateway）,当前实例的ip端口
//                    + URLEncoder.encode(uri.getScheme() + "://" + uri.getAuthority()
//                    // gateway回调地址参数 http://gateway/login/callback?url=
//                    + LOGIN_CALLBACK_PATH + "?url="
//                    // 登录成功重定向地址
//                    + URLEncoder.encode(uri.toString(), "UTF-8"), "UTF-8");
//        } catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//        }
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatus.SEE_OTHER);
//        response.getHeaders().set(HttpHeaders.LOCATION, url);
//        response.getHeaders().set("Content-Type", "text/plain; charset=utf-8");
//        return exchange.getResponse().setComplete();
//    }
//
//    public static String getLoginToken(ServerWebExchange exchange) {
//        if (null == exchange) {
//            return null;
//        }
//        ServerHttpRequest request = exchange.getRequest();
//        String loginToken = request.getHeaders().getFirst(TOKEN_NAME);
//        if (StringUtils.isBlank(loginToken)) {
//            Object token = exchange.getAttribute(TOKEN_NAME);
//            if (null != token) {
//                loginToken = (String) token;
//            }
//        }
//        if (StringUtils.isBlank(loginToken)) {
//            loginToken = request.getQueryParams().getFirst(TOKEN_NAME);
//        }
//        if (StringUtils.isBlank(loginToken)) {
//            HttpCookie loginCookie = request.getCookies().getFirst(TOKEN_NAME);
//            if (null != loginCookie) {
//                loginToken = loginCookie.getValue();
//            }
//        }
//        return loginToken;
//    }
//
//    /**
//     * 路由匹配
//     *
//     * @param patterns 路由匹配符集合
//     * @param path     被匹配的路由
//     * @return 是否匹配成功
//     */
//    public boolean isMatch(List<String> patterns, String path) {
//        for (String pattern : patterns) {
//            if (pathMatcher.match(pattern, path)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
