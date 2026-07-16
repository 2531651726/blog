package com.haha.blog.search.document;

import com.haha.blog.common.constants.DateConstants;
import com.haha.blog.common.domain.dos.ArticleContentDO;
import com.haha.blog.common.domain.dos.ArticleDO;
import com.haha.blog.search.index.ArticleIndex;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ArticleDocument {
    private Long id;
    private String title;
    private String cover;
    private String summary;
    private String content;
    private LocalDateTime createTime;

    public ArticleDocument(ArticleDO articleDO, ArticleContentDO articleContentDO) {
        this.id = articleDO.getId();
        this.title = articleDO.getTitle();
        this.cover = articleDO.getCover();
        this.summary = articleDO.getSummary();
        this.content = articleContentDO.getContent();
        this.createTime = articleDO.getCreateTime();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(ArticleIndex.COLUMN_ID, this.id);
        map.put(ArticleIndex.COLUMN_TITLE, this.title);
        map.put(ArticleIndex.COLUMN_COVER, this.cover);
        map.put(ArticleIndex.COLUMN_SUMMARY, this.summary);
        map.put(ArticleIndex.COLUMN_CONTENT, this.content);
        if (this.createTime != null) {
            map.put(ArticleIndex.COLUMN_CREATE_TIME, this.createTime.format(DateConstants.DATE_TIME_FORMATTER));
        } else {
            map.put(ArticleIndex.COLUMN_CREATE_TIME, null);
        }
        return map;
    }

}
