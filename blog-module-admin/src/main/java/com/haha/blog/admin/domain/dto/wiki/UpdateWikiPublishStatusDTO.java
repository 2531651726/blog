package com.haha.blog.admin.domain.dto.wiki;

import com.haha.blog.common.enums.PublishStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateWikiPublishStatusDTO {
    @NotNull(message = "知识库 ID 不能为空")
    private Long id;

    @NotNull(message = "知识库发布状态不能为空")
    private PublishStatus isPublish;
}
