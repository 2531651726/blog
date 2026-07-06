package com.haha.blog.admin.domain.dto.notice;

import com.haha.blog.common.enums.NoticeStatus;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddNoticeDTO {
    @NotBlank(message = "公告内容不能为空")
    @Length(min = 1, max = 460, message = "公告内容字数限制 1 ~ 460 之间")
    private String content;

    @NotNull(message = "是否展示不能为空")
    private NoticeStatus isShow;
}
