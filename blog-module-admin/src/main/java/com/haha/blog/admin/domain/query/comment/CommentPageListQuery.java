package com.haha.blog.admin.domain.query.comment;

import com.haha.blog.common.enums.CommentStatus;
import com.haha.blog.common.utils.BasePageQuery;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentPageListQuery extends BasePageQuery {
    /**
     * 路由地址
     */
    private String routerUrl;

    /**
     * 发布的起始日期
     */
    private LocalDate startDate;

    /**
     * 发布的结束日期
     */
    private LocalDate endDate;

    /**
     * 状态
     */
    private CommentStatus status;
}
