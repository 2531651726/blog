package com.haha.blog.web.domain.query.wiki;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WikiArticlePreNextQuery {
    @NotNull(message = "知识库 ID 不能为空")
    private Long id;

    @NotNull(message = "文章 ID 不能为空")
    private Long articleId;
}
