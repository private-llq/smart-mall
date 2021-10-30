package com.jsy.canstant;


import com.jsy.basic.util.RedisStateCache;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Slf4j
@Component
public class AllowAop {

    @Autowired
    private RedisStateCache redisStateCache;

    @Pointcut("@annotation(com.jsy.annotation.AllowRequest)")
    public void pointcut(){
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        try {
            if (redisStateCache.get(token)!=null){
                throw new JSYException(JSYError.FORBIDDEN.getCode(),"订单繁忙请稍后");
            }
            redisStateCache.cache(token,token,100);
            return point.proceed();
        }catch (Exception e){
            log.error("连接redis失败",e.getMessage());
            throw new JSYException(JSYError.INTERNAL.getCode(),"请稍后再试");
        }
        finally {
        }
    }
}
