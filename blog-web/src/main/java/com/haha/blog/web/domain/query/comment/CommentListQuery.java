package com.haha.blog.web.domain.query.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentListQuery {
    @NotBlank(message = "路由地址不能为空")
    private String routerUrl;
}
