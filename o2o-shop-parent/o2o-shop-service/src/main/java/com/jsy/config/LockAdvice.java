package com.jsy.config;

import com.jsy.basic.util.exception.JSYException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LockAdvice {

    @Resource
    private Redisson rc;

    @Around("execution(* *..service..*.*(..))&&@annotation(com.jsy.config.RidissonLock)")
    public Object lock(ProceedingJoinPoint pjp) {
        System.out.println("加锁成功");
        RLock lock = null;
        try {
            MethodSignature ms = (MethodSignature) pjp.getSignature();
            Method method = ms.getMethod();
            RidissonLock rl = method.getDeclaredAnnotation(RidissonLock.class);
            String className = pjp.getTarget().getClass().getName();//类的绝对路径
            String methodName = pjp.getSignature().getName();       //方法名
            lock = rc.getLock(className + ":" + methodName);
            boolean b = lock.tryLock(rl.time(), rl.lockTimeOut(),TimeUnit.SECONDS);
            if (b) {
                return pjp.proceed();
            }
        } catch (Throwable e) {

            if (e instanceof JSYException) {
                JSYException jsyException = (JSYException) e;
                throw new JSYException(jsyException.getCode(), e.getMessage());
            } else {
                e.printStackTrace();
                throw new RuntimeException(e);
            }


        } finally {
            System.out.println("解锁成功");
            lock.unlock(); // 释放锁
        }
        return null;
    }


}
