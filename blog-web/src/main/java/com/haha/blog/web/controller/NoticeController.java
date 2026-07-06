package com.haha.blog.web.controller;

import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.web.domain.vo.notice.NoticeVO;
import com.haha.blog.web.service.INoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
@Api(tags = "公告")
@RequiredArgsConstructor
public class NoticeController {

    private final INoticeService noticeService;

    @PostMapping("/info")
    @ApiOperation(value = "获取公告信息")
    @ApiOperationLog(description = "获取公告信息")
    public NoticeVO queryNoticeInfo() {
        return noticeService.queryNoticeInfo();
    }

}
