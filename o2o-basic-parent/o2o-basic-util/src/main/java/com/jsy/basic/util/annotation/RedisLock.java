package com.jsy.basic.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     * 锁的key
     * @return
     */
    String key();

    /**
     * 释放锁的时间
     * @return
     */
    int outTime();

    /**
     * 设置锁的值
     * @return
     */
    String value() default "0";
}
