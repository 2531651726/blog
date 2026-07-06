package com.haha.blog.admin.controller;

import com.haha.blog.admin.domain.dto.notice.AddNoticeDTO;
import com.haha.blog.admin.domain.dto.notice.DeleteNoticeDTO;
import com.haha.blog.admin.domain.dto.notice.UpdateNoticeDTO;
import com.haha.blog.admin.domain.dto.notice.UpdateNoticeStatusDTO;
import com.haha.blog.admin.domain.query.notice.NoticePageListQuery;
import com.haha.blog.admin.domain.vo.notice.NoticePageListVO;
import com.haha.blog.admin.service.INoticeAdminService;
import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.common.utils.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/notice")
@Api(tags = "admin 公告模块")
@RequiredArgsConstructor
public class NoticeAdminController {

    private final INoticeAdminService noticeService;

    @PostMapping("/add")
    @ApiOperation(value = "添加公告")
    @ApiOperationLog(description = "添加公告")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addNotice(@RequestBody @Validated AddNoticeDTO dto) {
        noticeService.addNotice(dto);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改公告")
    @ApiOperationLog(description = "修改公告")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateNotice(@RequestBody @Validated UpdateNoticeDTO dto) {
        noticeService.updateNotice(dto);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "公告删除")
    @ApiOperationLog(description = "公告删除")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteNotice(@RequestBody @Validated DeleteNoticeDTO dto) {
        noticeService.deleteNotice(dto);
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询公告分页数据")
    @ApiOperationLog(description = "查询公告分页数据")
    public PageDTO<NoticePageListVO> queryNoticePageList(@RequestBody @Validated NoticePageListQuery query) {
        return noticeService.queryNoticePageList(query);
    }

    @PostMapping("/isShow/update")
    @ApiOperation(value = "公告展示状态修改")
    @ApiOperationLog(description = "公告展示状态修改")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateNoticeStatus(@RequestBody @Validated UpdateNoticeStatusDTO dto) {
        noticeService.updateNoticeStatus(dto);
    }
}
