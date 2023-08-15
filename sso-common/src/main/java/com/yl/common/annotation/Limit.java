package com.yl.common.annotation;

import com.yl.common.config.LimitType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limit {

    /**
     * 限流key前缀
     */
    String prefix() default "limit:";


    /**
     * 限流时间,单位秒
     */
    int time() default 60;


    /**
     * 限流次数
     */
    int count() default 10;


    /**
     * 限流类型
     */
    LimitType type() default LimitType.DEFAULT;

}
