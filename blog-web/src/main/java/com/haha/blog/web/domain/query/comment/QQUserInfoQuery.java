package com.haha.blog.web.domain.query.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QQUserInfoQuery {
    @NotBlank(message = "QQ 号不能为空")
    private String qq;
}
