package com.haha.blog.web.domain.vo.category;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class CategoryArticleVO {
    private Long id;
    private String cover;
    private String title;
    private LocalDate createDate;
}
