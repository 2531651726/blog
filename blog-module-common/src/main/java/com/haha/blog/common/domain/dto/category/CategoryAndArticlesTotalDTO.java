package com.haha.blog.common.domain.dto.category;

import lombok.Data;

@Data
public class CategoryAndArticlesTotalDTO {
    private Long categoryId;
    private Integer articlesTotal;
}
