package com.haha.blog.admin.runner;

import com.haha.blog.admin.service.IStatisticsAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticsTagArticleTotalRunner implements CommandLineRunner {

    private final IStatisticsAdminService statisticsAdminService;

    @Override
    @Async("threadPoolTaskExecutor")
    public void run(String... args) throws Exception {
        log.info("==> 开始统计各标签下文章数量...");
        statisticsAdminService.statisticsTagArticleTotal();
        log.info("==> 结束统计各标签下文章数量...");
    }
}
