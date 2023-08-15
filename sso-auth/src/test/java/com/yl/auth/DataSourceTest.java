package com.yl.auth;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.yl.auth.config.TestConfig;
import com.yl.auth.mapper.DeadmanMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
public class DataSourceTest {

    @Autowired
    DeadmanMapper deadmanMapper;

    @Test
    @DS("test")
    public void contextLoads() {
        deadmanMapper.selectById("1");
    }

    @Test
    public void print() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
    }

//    public static void main(String[] args) throws IOException {
//        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
//        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
//        Enumeration<URL> urls = contextClassLoader.getResources("META-INF/spring.factories");
//        while (urls.hasMoreElements()) {
//            URL url = urls.nextElement();
//            System.out.println(url.getPath());
//        }

}
