package com.haha.blog.web.domain.query.wiki;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WikiCatalogQuery {

    @NotNull(message = "知识库 ID 不能为空")
    private Long id;
}
