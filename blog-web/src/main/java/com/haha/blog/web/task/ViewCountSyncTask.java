package com.haha.blog.web.task;

import com.haha.blog.common.service.ArticleViewCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ViewCountSyncTask {

    private final ArticleViewCountService articleViewCountService;

    @Scheduled(fixedDelay = 4 * 60 * 1000)
    public void syncArticleViewCountFromRedisToDb() {
        long start = System.currentTimeMillis();
        log.info("==> 开始执行文章阅读量同步定时任务");
        try {
            articleViewCountService.syncViewCountToDatabase();
        } catch (Exception e) {
            log.error("浏览量定时同步任务异常", e);
        }
        long cost = System.currentTimeMillis() - start;
        log.info("==> 文章阅读量同步定时任务执行结束,耗时：{} ms", cost);
    }


}
