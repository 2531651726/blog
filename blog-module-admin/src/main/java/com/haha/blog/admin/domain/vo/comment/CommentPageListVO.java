package com.haha.blog.admin.domain.vo.comment;

import com.haha.blog.common.enums.CommentStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CommentPageListVO {
    private Long id;

    private String routerUrl;

    private String avatar;

    private String nickname;

    private String mail;

    private String website;

    private LocalDateTime createTime;

    private String content;

    private CommentStatus status;

    private String reason;
}
