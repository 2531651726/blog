package com.haha.blog.web.domain.vo.wiki;

import lombok.Data;

@Data
public class WikiVO {
    private Long id;
    private String cover;
    private String title;
    private String summary;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 第一篇文章 ID
     */
    private Long firstArticleId;
}
