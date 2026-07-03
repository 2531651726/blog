package com.haha.blog.admin.domain.query.wiki;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WikiCatalogListQuery {
    @NotNull(message = "知识库 ID 不能为空")
    private Long id;

}
