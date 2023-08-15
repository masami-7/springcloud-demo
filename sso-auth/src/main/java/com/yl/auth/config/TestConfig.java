package com.yl.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@EnableAspectJAutoProxy
public class TestConfig {

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event){
        System.out.println();
    }

}
