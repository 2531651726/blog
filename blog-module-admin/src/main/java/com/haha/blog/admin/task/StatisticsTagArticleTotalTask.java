package com.haha.blog.admin.task;

import com.haha.blog.admin.service.IStatisticsAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StatisticsTagArticleTotalTask {

    private final IStatisticsAdminService statisticsAdminService;

    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨 2 点执行
    // 定时任务保证标签下文章数量统计的数据一致性
    public void execute() {
        log.info("==> 开始统计各标签下文章数量...");
        statisticsAdminService.statisticsTagArticleTotal();
        log.info("==> 结束统计各标签下文章数量...");
    }
}
