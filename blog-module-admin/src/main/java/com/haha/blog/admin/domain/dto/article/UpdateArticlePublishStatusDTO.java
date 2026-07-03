package com.haha.blog.admin.domain.dto.article;

import com.haha.blog.common.enums.PublishStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateArticlePublishStatusDTO {
    @NotNull(message = "文章 ID 不能为空")
    private Long id;

    @NotNull(message = "文章发布状态不能为空")
    private PublishStatus isPublish;

}
