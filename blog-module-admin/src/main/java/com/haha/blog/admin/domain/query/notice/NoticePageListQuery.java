package com.haha.blog.admin.domain.query.notice;

import com.haha.blog.common.enums.NoticeStatus;
import com.haha.blog.common.utils.BasePageQuery;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NoticePageListQuery extends BasePageQuery {

    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private NoticeStatus isShow;
}
