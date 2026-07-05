package com.haha.blog.admin.domain.dto.comment;

import com.haha.blog.common.enums.CommentStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExamineCommentDTO {
    @NotNull(message = "评论 ID 不能为空")
    private Long id;

    @NotNull(message = "评论状态不能为空")
    private CommentStatus status;

    private String reason;
}
