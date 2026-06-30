package com.haha.blog.web.domain.vo.category;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryVO {
    private Long id;
    private String name;
    private Integer articlesTotal;
}
