package com.haha.blog.admin.event.listener;

import com.haha.blog.admin.event.message.ArticleChangeMessage;
import com.haha.blog.admin.event.message.ArticleChangeMessage.ChangeType;
import com.haha.blog.admin.service.IStatisticsAdminService;
import com.haha.blog.common.domain.dos.ArticleContentDO;
import com.haha.blog.common.domain.dos.ArticleDO;
import com.haha.blog.common.mapper.ArticleContentMapper;
import com.haha.blog.common.mapper.ArticleMapper;
import com.haha.blog.search.document.ArticleDocument;
import com.haha.blog.search.index.ArticleIndex;
import com.haha.blog.search.utils.ElasticsearchHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleEventListener {

    private final IStatisticsAdminService statisticsAdminService;
    private final ElasticsearchHelper elasticsearchHelper;
    private final ArticleMapper articleMapper;
    private final ArticleContentMapper articleContentMapper;

    // ==================== 统计更新 ====================

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("threadPoolTaskExecutor")
    public void handleCategory(ArticleChangeMessage message) {
        log.info("监听到文章变更事件，开始执行分类文章数量统计");
        statisticsAdminService.statisticsCategoryArticleTotal();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("threadPoolTaskExecutor")
    public void handleTag(ArticleChangeMessage message) {
        log.info("监听到文章变更事件，开始执行标签文章数量统计");
        statisticsAdminService.statisticsTagArticleTotal();
    }

    // ==================== ES 文档同步 ====================

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("threadPoolTaskExecutor")
    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void handleEsSync(ArticleChangeMessage message) {
        Long articleId = message.getArticleId();
        ChangeType changeType = message.getChangeType();
        log.info("事务已提交，开始同步ES，文章ID[{}]，变更类型[{}]", articleId, changeType);

        switch (changeType) {
            case CREATE:
            case UPDATE: {
                // 从数据库查询最新数据
                ArticleDO articleDO = articleMapper.selectById(articleId);
                if (articleDO == null) {
                    log.warn("文章[{}]不存在，跳过ES同步", articleId);
                    return;
                }
                ArticleContentDO contentDO = articleContentMapper.selectByArticleId(articleId);
                if (contentDO == null) {
                    log.warn("文章[{}]正文不存在，跳过ES同步", articleId);
                    return;
                }
                ArticleDocument doc = new ArticleDocument(articleDO, contentDO);
                Map<String, Object> source = doc.toMap();
                elasticsearchHelper.fullUpdateDocument(ArticleIndex.NAME,
                        articleId.toString(), source);
                log.info("ES文档同步成功（新增/修改），文章ID[{}]", articleId);
                break;
            }
            case DELETE:
                elasticsearchHelper.deleteDocument(ArticleIndex.NAME, articleId.toString());
                log.info("ES文档删除成功，文章ID[{}]", articleId);
                break;
            default:
                log.warn("未知的文章变更类型[{}]，跳过ES同步", changeType);
        }
    }

    /**
     * 重试耗尽后的兜底处理（仅记录错误日志）
     */
    @Recover
    public void recoverEsSync(Exception e, ArticleChangeMessage message) {
        log.error("ES文档同步失败（重试3次已耗尽），文章ID[{}]，变更类型[{}]，异常信息：{}",
                message.getArticleId(), message.getChangeType(), e.getMessage(), e);
    }
}
