package com.haha.blog.admin.domain.dto.notice;

import com.haha.blog.common.enums.NoticeStatus;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Data
public class UpdateNoticeStatusDTO {
    @NotNull(message = "公告 ID 不能为空")
    private Long id;

    @NotNull(message = "公告状态不能为空")
    private NoticeStatus isShow;
}
