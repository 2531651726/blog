package com.haha.blog.web.domain.query.tag;

import com.haha.blog.common.utils.BasePageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TagArticlePageQuery extends BasePageQuery {
    @NotNull(message = "标签 ID 不能为空")
    private Long id;

}
