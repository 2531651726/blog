package com.haha.blog.admin.runner;

import com.haha.blog.admin.service.IStatisticsAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitPVRecordRunner implements CommandLineRunner {

    private final IStatisticsAdminService statisticsService;

    @Override
    @Async("threadPoolTaskExecutor")
    public void run(String... args) throws Exception {
        log.info("==> 开始初始化 PV 访问量记录...");
        statisticsService.initPvRecord(LocalDate.now());
        statisticsService.initPvRecord(LocalDate.now().plusDays(1));
        log.info("==> 结束初始化 PV 访问量记录...");
    }
}
