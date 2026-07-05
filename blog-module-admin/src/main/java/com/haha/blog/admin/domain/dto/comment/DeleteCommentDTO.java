package com.haha.blog.admin.domain.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteCommentDTO {
    @NotNull(message = "评论 ID 不能为空")
    private Long id;
}
