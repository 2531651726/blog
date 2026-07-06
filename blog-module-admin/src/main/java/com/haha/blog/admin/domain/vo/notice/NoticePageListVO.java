package com.haha.blog.admin.domain.vo.notice;

import com.haha.blog.common.enums.NoticeStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticePageListVO {

    private Long id;
    private String content;
    private NoticeStatus isShow;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
