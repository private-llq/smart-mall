package com.jsy.canstant;


import com.alibaba.fastjson.JSONObject;
import com.jsy.annotation.ServiceAnnotation;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.domain.OrderLog;
import com.jsy.service.IOrderLogService;
import com.jsy.service.IOrderService;
import com.jsy.service.IShopRecordService;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 切面
 * 完成对controller和service层的错误定位，以及异常信息
 * @author yu
 */

@Aspect
@Component
public class WebRequestAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IShopRecordService shopRecordService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IOrderLogService orderLogService;

    @Value("暂定")
    private String PERSON;


    /*
    * @AspectJ提供不同的通知类型
    ····@Before前置通知，相当于BeforeAdvice
    ····@AfterReturning后置通知，相当于AfterReturningAdvice
    ····@Around环绕通知，相当于MethodInterceptor
    ····@AfterThrowing抛出通知，相当于ThrowAdvice
    ····@After最终final通知，不管是否异常，该通知都会执行
    * */

    /**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("@annotation(com.jsy.annotation.ControllerAnnotation)")'
     */
    //@Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    @Pointcut("@annotation(com.jsy.annotation.ControllerAnnotation)")
    public void operationLog(){
    }

    @Pointcut("@annotation(com.jsy.annotation.ServiceAnnotation)")
    public void operationServiceLog(){
    }

    @Pointcut("execution(public * com.jsy.controller.*.*(..))")
    public void operationLogController(){
    }

    /**
     * 进入业务方法前
     * */
    @Before("operationServiceLog()")
    public void doBeforeAdviceService(JoinPoint joinPoint){
        System.out.println("进入service前执行.....");
    }

    /**
     * 环绕通知
     * */
    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String params = "";
        if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params += JSONObject.toJSONString(joinPoint.getArgs()[i]) + ";";
            }
        }
        //Object ret  =joinPoint.proceed(joinPoint.getArgs());
        Object proceed = null;

        try {
            //执行增强后的方法
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", throwable.getMessage());

            //如果是本地自定义异常就不用记录
            if (throwable instanceof JSYException) {
                JSYException jsyException = (JSYException) throwable;
                throw jsyException;
            }

            throwable.printStackTrace();
            //记录本地异常日志
            OrderLog log = new OrderLog();
            log.setDescription(getControllerMethodDescription(joinPoint));
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setRequestIp(ip);
            log.setPerson(PERSON);
            log.setType("1");
            log.setExceptionCode(throwable.getClass().getName());
            log.setExceptionDetail( throwable.getMessage());
            log.setParams(null);
            orderLogService.save(log);



        } finally {

        }
        System.out.println("进入切面");
        System.out.println("--------------"+proceed);

        return proceed;
    }
    /**
     * 环绕通知
     * */
    @Around("operationServiceLog()")
    public Object doAroundServie(ProceedingJoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();


        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String params = "";
        if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params += JSONObject.toJSONString(joinPoint.getArgs()[i]) + ";";
            }
        }
        //Object ret  =joinPoint.proceed(joinPoint.getArgs());
        Object proceed = null;
        OrderLog log = new OrderLog();
        try {
            log.setDescription(getServiceMethodDescription(joinPoint));
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setRequestIp(ip);
            log.setPerson(PERSON);
            if (method.isAnnotationPresent(ServiceAnnotation.class)) {
                log.setType("0");
                log.setExceptionCode( null);
                log.setExceptionDetail( null);
                log.setParams(null);
            }
            //执行增强后的方法
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", throwable.getMessage());
            throwable.printStackTrace();
            if (throwable instanceof JSYException) {
                JSYException jsyException = (JSYException) throwable;
                throw jsyException;
            }
            log.setType("1");
            log.setExceptionCode(throwable.getClass().getName());
            log.setExceptionDetail(throwable.getMessage());
            log.setParams(null);
            orderLogService.save(log);
        } finally {
        }
        System.out.println("进入切面");
        System.out.println("--------------"+proceed);

        return proceed;
    }
    /**
     * 进入业务方法前
     * */
    //@Before("operationLog()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();
        try {
            OrderLog log = new OrderLog();
            log.setDescription(getControllerMethodDescription(joinPoint));
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setType("0");
            log.setRequestIp(ip);
            log.setExceptionCode( null);
            log.setExceptionDetail( null);
            log.setParams(null);
            log.setPerson(PERSON);
            //log.setCreateBy(user);

            orderLogService.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("进入方法前执行....."+PERSON + ip);
    }

    /**
     * 处理完请求，返回内容
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "operationLog()")
    public void doAfterReturning(Object ret) {
        System.out.println("方法的返回值 : " + ret);
    }

    /**
     * 后置异常通知
     */
    //@AfterThrowing(value = "operationLog()",throwing = "throwable")
    public void throwss(JoinPoint joinPoint,Throwable throwable){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();

        /*从切面值入点获取植入点方法*/
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        /*获取切入点方法*/
        Method method = signature.getMethod();
        String params = "";
        if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params += JSONObject.toJSONString(joinPoint.getArgs()[i]) + ";";
            }
        }
        try {
            /*========控制台输出=========*/
            System.out.println("=====异常通知开始=====");
            System.out.println("异常代码:" + throwable.getClass().getName());
            System.out.println("异常信息:" + throwable.getMessage());
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("方法描述:" + getControllerMethodDescription(joinPoint));
            System.out.println("请求人:" + PERSON);
            System.out.println("请求IP:" + ip);
            System.out.println("请求参数:" + params);

            OrderLog log = new OrderLog();
            log.setDescription(getControllerMethodDescription(joinPoint));
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setType("1");
            log.setRequestIp(ip);
            log.setExceptionCode(throwable.getClass().getName());
            log.setExceptionDetail( throwable.getMessage());
            //log.setParams(params);
            log.setPerson(PERSON);
            //log.setCreateBy(user);
            orderLogService.save(log);
            System.out.println("=====异常通知结束=====");
        } catch (Exception e) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", throwable.getMessage());
            e.printStackTrace();
        }
        System.out.println("方法异常时执行.....");
    }

    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     */
    @After("operationLog()")
    public void after(JoinPoint jp){
        System.out.println("方法最后执行.....");
    }

    /**
     * 获取注解中对方法的描述信息 用于service层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public  static String getServiceMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(ServiceAnnotation. class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public  static String getControllerMethodDescription(JoinPoint joinPoint)  {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(ApiOperation.class).value();
                    break;
                }
            }
        }
        return description;
    }

}

