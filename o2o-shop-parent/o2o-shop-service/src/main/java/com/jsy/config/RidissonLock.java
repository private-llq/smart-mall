package com.jsy.config;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RidissonLock {
    // String lockKey();
    int time() default 20;
    int lockTimeOut() default 20;

    String start() default "";   //前缀

    String end() default "";     //后缀

    int index() default 0;       //索引

    String fileName() default ""; //字段名
}
