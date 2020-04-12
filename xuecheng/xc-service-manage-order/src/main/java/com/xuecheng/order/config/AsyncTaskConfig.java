package com.xuecheng.order.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Configuration      // 标识配置类, 由Spring容器管理
@EnableScheduling   // 开启定时任务
public class AsyncTaskConfig implements SchedulingConfigurer,AsyncConfigurer{

    // 定义线程数量
    private static final int CORE_POOL_SIZE = 5;


    @Bean
    public ThreadPoolTaskScheduler getThreadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        // 初始化
        threadPoolTaskScheduler.initialize();
        // 设置线程数量
        threadPoolTaskScheduler.setPoolSize(CORE_POOL_SIZE);
        return threadPoolTaskScheduler;

    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setTaskScheduler(this.getThreadPoolTaskScheduler());
    }

    @Override
    public Executor getAsyncExecutor() {
        Executor executor = this.getThreadPoolTaskScheduler();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}