package com.haha.blog.admin.event.listener;

import com.haha.blog.admin.event.message.ArticleChangeMessage;
import com.haha.blog.admin.service.IStatisticsAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleEventListener {

    private final IStatisticsAdminService statisticsAdminService;

    @EventListener(ArticleChangeMessage.class)
    @Async("threadPoolTaskExecutor")
    public void handleCategory(ArticleChangeMessage message) {
        log.info("监听到文章变更事件，开始执行分类文章数量统计");
        statisticsAdminService.statisticsCategoryArticleTotal();
    }

    @EventListener(ArticleChangeMessage.class)
    @Async("threadPoolTaskExecutor")
    public void handleTag(ArticleChangeMessage message) {
        log.info("监听到文章变更事件，开始执行标签文章数量统计");
        statisticsAdminService.statisticsTagArticleTotal();
    }
}
