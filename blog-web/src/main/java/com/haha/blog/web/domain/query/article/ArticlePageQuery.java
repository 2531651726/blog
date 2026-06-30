package com.haha.blog.web.domain.query.article;

import com.haha.blog.common.utils.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "首页查询文章分页 query")
public class ArticlePageQuery extends BasePageQuery {


}
