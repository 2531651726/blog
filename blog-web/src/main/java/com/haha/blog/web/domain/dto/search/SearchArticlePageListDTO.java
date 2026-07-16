package com.haha.blog.web.domain.dto.search;

import com.haha.blog.common.utils.BasePageQuery;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SearchArticlePageListDTO extends BasePageQuery {
    @NotBlank(message = "搜索关键词不能为空")
    private String word;
}
