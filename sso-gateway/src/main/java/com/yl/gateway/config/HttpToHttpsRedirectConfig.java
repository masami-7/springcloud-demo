package com.yl.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class HttpToHttpsRedirectConfig {

    @Value("${server.http.port}")
    private int httpPort;
    @Value("${server.port}")
    private int serverPort;
    @Autowired
    private HttpHandler httpHandler;

    @PostConstruct
    public void startRedirectServer() {
        NettyReactiveWebServerFactory httpNettyReactiveWebServerFactory = new NettyReactiveWebServerFactory(httpPort);
        httpNettyReactiveWebServerFactory.getWebServer((request, response) -> {
            URI uri = request.getURI();
            if (isNeedRedirect(uri.getPath())) {
                URI httpsUri;
                try {
                    httpsUri = new URI("https", uri.getUserInfo(), uri.getHost(), serverPort, uri.getPath(), uri.getQuery(), uri.getFragment());
                } catch (URISyntaxException e) {
                    return Mono.error(e);
                }
                response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                response.getHeaders().setLocation(httpsUri);
                return response.setComplete();
            } else {
                return httpHandler.handle(request, response);
            }
        }).start();
    }

    private boolean isNeedRedirect(String path) {
        return !path.startsWith("/actuator");
    }

}
