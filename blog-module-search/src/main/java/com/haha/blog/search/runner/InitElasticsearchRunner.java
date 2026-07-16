package com.haha.blog.search.runner;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.haha.blog.common.domain.dos.ArticleContentDO;
import com.haha.blog.common.domain.dos.ArticleDO;
import com.haha.blog.common.mapper.ArticleContentMapper;
import com.haha.blog.common.mapper.ArticleMapper;
import com.haha.blog.search.document.ArticleDocument;
import com.haha.blog.search.index.ArticleIndex;
import com.haha.blog.search.utils.BulkWriteItem;
import com.haha.blog.search.utils.ElasticsearchHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitElasticsearchRunner implements CommandLineRunner {

    private final ElasticsearchHelper elasticsearchHelper;
    private final ArticleMapper articleMapper;
    private final ArticleContentMapper articleContentMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("==> 开始初始化 elasticsearch 文章索引...");
        // 创建索引（已存在则跳过）
        elasticsearchHelper.createIndex(ArticleIndex.NAME, ArticleIndex.MAPPING);
        // 查询所有文章
        List<ArticleDO> articleDOS = articleMapper.selectList(Wrappers.emptyWrapper());
        if (articleDOS.isEmpty()) {
            log.info("==> 数据库暂无文章，索引已创建，跳过文档同步...");
            return;
        }
        // 批量查询所有文章正文，按 articleId 组装为 Map，避免 N+1 查询
        List<ArticleContentDO> contents = articleContentMapper.selectList(Wrappers.emptyWrapper());
        Map<Long, ArticleContentDO> contentMap = contents.stream()
                .collect(Collectors.toMap(ArticleContentDO::getArticleId, Function.identity(), (a, b) -> a));
        // 封装文档集合
        List<BulkWriteItem> list = new ArrayList<>();
        for (ArticleDO articleDO : articleDOS) {
            Long articleId = articleDO.getId();
            ArticleContentDO articleContentDO = contentMap.get(articleId);
            if (articleContentDO == null) {
                log.warn("文章[{}]未找到对应正文，跳过该文章", articleId);
                continue;
            }
            ArticleDocument articleDocument = new ArticleDocument(articleDO, articleContentDO);
            Map<String, Object> map = articleDocument.toMap();
            BulkWriteItem bulkWriteItem = new BulkWriteItem()
                    .setId(articleId.toString())
                    .setSource(map);
            list.add(bulkWriteItem);
        }
        // 批量写入文档
        elasticsearchHelper.bulkWriteDocuments(ArticleIndex.NAME, list);
        log.info("==> 文章文档同步完成，共处理[{}]篇", list.size());
    }
}
