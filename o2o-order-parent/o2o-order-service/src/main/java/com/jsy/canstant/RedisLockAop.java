package com.jsy.canstant;

import com.jsy.basic.util.RedisStateCache;
import com.jsy.basic.util.annotation.RedisLock;
import com.jsy.basic.util.aspectj.BaseAop;
import com.jsy.basic.util.exception.JSYException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Slf4j
@Component
public class RedisLockAop extends BaseAop{

    @Value("smart-mall:")
    private String prefix;


    @Qualifier("stringRedisTemplate")
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private RedisStateCache redisStateCache;

    @Pointcut("@annotation(com.jsy.basic.util.annotation.RedisLock)")
    public void pointcut(){
    }
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = getMethod(point.getTarget().getClass(), methodSignature);
        RedisLock annotation = method.getAnnotation(RedisLock.class);
        String key = annotation.key();
        int lockTime = annotation.outTime();
        String value =annotation.value();
        // 定义锁
        String lock = null;
        try {
            //心跳
            while (true){
                lock = redisStateCache.get(prefix + key);
                if ("".equals(lock)||lock==null) {
                    redisStateCache.cache(prefix+key,value,lockTime);
                    // 执行业务方法
                    return point.proceed();
                }
                Thread.sleep(1000);

            }
        } catch (Exception e) {
            log.error(this.getClass().getPackageName() + ".singleInstanceLockAround：{}", e.getCause());
            throw new JSYException(0, "方法执行失败, 请重试!");
        } finally {
            if (StringUtils.isNotEmpty(redisStateCache.get(prefix + key))){
                //
            }
        }
        //失败最典型的原因是redis挂了
        //throw new JSYException(0, "加锁失败, 本次操作取消, 请重试!");
    }
}
