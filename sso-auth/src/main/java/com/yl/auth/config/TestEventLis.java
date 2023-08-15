package com.yl.auth.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestEventLis implements ApplicationListener<ContextRefreshedEvent> {

    public TestEventLis() {
        System.out.println();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println();
    }

}
