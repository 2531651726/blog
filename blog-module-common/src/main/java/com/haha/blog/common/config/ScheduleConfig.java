package com.haha.blog.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
@EnableScheduling   // 开启定时任务
public class ScheduleConfig implements SchedulingConfigurer {
    /**
     * 定义定时任务专用线程池
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 核心线程数：同时能并行执行多少个定时任务
        scheduler.setPoolSize(5);
        // 线程前缀，方便日志排查
        scheduler.setThreadNamePrefix("blog-schedule-");
        // 拒绝策略：任务满了由调用者线程执行
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 服务关闭后不再执行后续定时任务
        scheduler.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        // 容器关闭时不会直接杀掉正在运行的定时任务
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 最长等待 60 秒
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setErrorHandler(throwable -> log.error("定时任务执行异常", throwable));
        return scheduler;
    }

    /**
     * 将自定义定时线程池注册到调度器中，替换默认单线程池
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }
}
