package com.haha.blog.web.domain.query.archive;

import com.haha.blog.common.utils.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "文章归档分页 query")
public class ArchiveArticlePageQuery extends BasePageQuery {
}
