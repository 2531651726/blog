package com.haha.blog.admin.domain.dto.notice;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateNoticeDTO {
    @NotNull(message = "公告 ID 不能为空")
    private Long id;

    @NotBlank(message = "公告内容不能为空")
    @Length(min = 1, max = 460, message = "公告内容字数限制 1 ~ 460 之间")
    private String content;
}
