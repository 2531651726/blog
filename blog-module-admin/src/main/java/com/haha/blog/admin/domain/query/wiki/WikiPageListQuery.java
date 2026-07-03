package com.haha.blog.admin.domain.query.wiki;

import com.haha.blog.common.utils.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(value = "查询知识库分页数据入参 query")
public class WikiPageListQuery extends BasePageQuery {

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
}
