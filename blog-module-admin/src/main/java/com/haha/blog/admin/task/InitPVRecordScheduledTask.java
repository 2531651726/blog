package com.haha.blog.admin.task;

import com.haha.blog.admin.service.IStatisticsAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitPVRecordScheduledTask {

    private final IStatisticsAdminService statisticsService;

    @Scheduled(cron = "0 0 23 * * ?") // 每天晚间 23 点执行
    public void execute() {
        // 定时任务执行的业务逻辑
        log.info("==> 开始执行创建明日 PV 访问量记录表定时任务");
        statisticsService.initPvRecord(LocalDate.now().plusDays(1));
        log.info("==> 结束执行创建明日 PV 访问量记录表定时任务");
    }
}
