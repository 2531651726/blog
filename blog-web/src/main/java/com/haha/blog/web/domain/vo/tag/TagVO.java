package com.haha.blog.web.domain.vo.tag;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TagVO {
    private Long id;
    private String name;
    private Integer articlesTotal;
}
