package com.haha.blog.web.domain.vo.archive;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Accessors(chain = true)
public class ArchiveArticleVO {
    private Long id;
    private String cover;
    private String title;
    /**
     * 发布日期
     */
    private LocalDate createDate;

    /**
     * 发布的月份（此字段不需要展示在前端，主要用于按月份分组使用）
     */
    private YearMonth createMonth;
}
