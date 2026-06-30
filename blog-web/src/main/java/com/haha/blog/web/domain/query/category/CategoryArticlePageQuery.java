package com.haha.blog.web.domain.query.category;

import com.haha.blog.common.utils.BasePageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CategoryArticlePageQuery extends BasePageQuery {
    @NotNull(message = "分类 ID 不能为空")
    private Long id;
}
