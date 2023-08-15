package com.yl.auth.config;

import com.baomidou.dynamic.datasource.processor.DsHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.processor.jakarta.DsJakartaHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.jakarta.DsJakartaSessionProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;

@Configuration
public class DataSourceConfig {

    @Bean
    public DsProcessor dsProcessor(BeanFactory beanFactory) {
        String version = SpringBootVersion.getVersion();
        Object headerProcessor;
        Object sessionProcessor;
        if (version.startsWith("3")) {
            headerProcessor = new DsJakartaHeaderProcessor();
            sessionProcessor = new DsJakartaSessionProcessor();
        } else {
            headerProcessor = new DsHeaderProcessor();
            sessionProcessor = new DsSessionProcessor();
        }

        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        // 可以修改@DS注解怎么获取数据源的key
        ((DsProcessor)headerProcessor).setNextProcessor((DsProcessor)sessionProcessor);
        ((DsProcessor)sessionProcessor).setNextProcessor(spelExpressionProcessor);
        return (DsProcessor)headerProcessor;
    }

}
