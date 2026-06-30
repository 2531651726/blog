package com.haha.blog.web.domain.query.article;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "查询文章详情 query")
public class ArticleDetailQuery {

    /**
     * 文章 ID
     */
    @NotNull(message = "文章ID不能为空")
    private Long articleId;
}
