package com.jsy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    private static final long DEFAULT_KEEPALIVE_MILLIS = 10L;

    //针对定时任务调度-配置多线程（线程池）
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
        /*ThreadFactory builder = new ThreadFactoryBuilder().setNameFormat("configureTasks").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10
                , 200
                , 0L
                , TimeUnit.MILLISECONDS
                , new LinkedBlockingDeque<Runnable>(1024)
                , builder
                , new ThreadPoolExecutor.AbortPolicy());

        */

    }
}