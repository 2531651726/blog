package com.haha.blog.admin.domain.dto.notice;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteNoticeDTO {
    @NotNull(message = "公告 ID 不能为空")
    private Long id;
}
