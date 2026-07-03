package com.haha.blog.common.service;

import com.haha.blog.common.constants.RedisConstants;
import com.haha.blog.common.mapper.ArticleMapper;
import com.haha.blog.common.mapper.StatisticsArticlePvMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleViewCountService {
    private final StringRedisTemplate redisTemplate;
    private final ArticleMapper articleMapper;
    private final StatisticsArticlePvMapper statisticsArticlePvMapper;

    /**
     * 增加文章浏览量
     *
     * @param articleId 文章ID
     */
    public void incrementViewCount(Long articleId) {
        redisTemplate.opsForHash()
                .increment(RedisConstants.ARTICLE_VIEW_COUNT_DELTA_KEY, String.valueOf(articleId), 1L);
    }

    /**
     * 获取单篇文章阅读量 Redis 增量
     *
     * @param articleId 文章ID
     * @return 增量值
     */
    public Long getViewCountDelta(Long articleId) {
        Object value = redisTemplate.opsForHash()
                .get(RedisConstants.ARTICLE_VIEW_COUNT_DELTA_KEY, String.valueOf(articleId));
        return parseLong(value);
    }

    /**
     * 批量获取文章阅读量 Redis 增量
     *
     * @param articleIds 文章ID集合
     * @return key: 文章ID, value: 增量值
     */
    public Map<Long, Long> getBatchViewCountDelta(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Object> keys = articleIds.stream().map(String::valueOf).collect(Collectors.toList());
        List<Object> values = redisTemplate.opsForHash()
                .multiGet(RedisConstants.ARTICLE_VIEW_COUNT_DELTA_KEY, keys);
        Map<Long, Long> result = new HashMap<>(articleIds.size());
        for (int i = 0; i < articleIds.size(); i++) {
            Long articleId = articleIds.get(i);
            Long delta = parseLong(values.get(i));
            if (delta > 0) {
                result.put(articleId, delta);
            }
        }
        return result;
    }

    /**
     * 将 Redis 中的阅读量增量同步到数据库，并清空已同步的增量，同时通过 SUM 查询计算总浏览量差值, 更新当日 PV 统计
     */
    public void syncViewCountToDatabase() {
        try {
            syncArticleReadNumDelta();
        } catch (Exception e) {
            log.error("同步文章阅读量增量失败", e);
        }
        try {
            syncDailyPvCount();
        } catch (Exception e) {
            log.error("同步当日 PV 失败", e);
        }
    }

    /**
     * 同步文章阅读量增量到数据库
     */
    private void syncArticleReadNumDelta() {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> deltaMap = hashOps.entries(RedisConstants.ARTICLE_VIEW_COUNT_DELTA_KEY);
        if (deltaMap.isEmpty()) {
            return;
        }
        // 1. 先封装待更新数据
        Map<Long, Long> readNumMap = deltaMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Long.valueOf(entry.getKey()),
                        entry -> parseLong(entry.getValue())
                ));
        try {
            // 2. 更新数据库
            articleMapper.updateReadNumBatch(readNumMap);
            // 3. 更新成功后，再删除 Redis 缓存
            redisTemplate.opsForHash().delete(RedisConstants.ARTICLE_VIEW_COUNT_DELTA_KEY,
                    deltaMap.keySet().toArray());
            log.info("==> 同步文章阅读量到数据库完成, 共 {} 条", readNumMap.size());
        } catch (Exception e) {
            log.error("==> 同步文章阅读量到数据库失败", e);
        }
    }

    /**
     * 通过 SUM(read_num) 查询当前总浏览量, 与 Redis 中的上次基准值求差得到本次增量 PV,
     * 累加到当日 PV 统计记录, 并更新基准值
     */
    private void syncDailyPvCount() {
        Long currentTotal = articleMapper.queryTotalViewCount();
        if (currentTotal == null) {
            currentTotal = 0L;
        }
        String oldTotalStr = redisTemplate.opsForValue()
                .getAndSet(RedisConstants.ARTICLE_TOTAL_VIEW_COUNT_KEY,
                        String.valueOf(currentTotal));
        if(oldTotalStr == null || oldTotalStr.isEmpty()) {
            return;
        }
        Long lastTotal;
        try {
            lastTotal = parseLong(oldTotalStr);
        } catch (NumberFormatException e) {
            log.warn("==> redis总浏览量缓存返回非法数据：{}", oldTotalStr);
            return;
        }
        long delta = currentTotal - lastTotal;
        if (delta > 0) {
            statisticsArticlePvMapper.incrementPvCount(LocalDate.now(), delta);
        }
        log.info("==> 同步当日 PV 完成, 当前总浏览量: {}, 本次新增: {}", currentTotal, delta);
    }

    /**
     * 解析 Redis 返回的值为 Long
     */
    private Long parseLong(Object value) {
        if (Objects.isNull(value)) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.valueOf(value.toString());
    }
}
