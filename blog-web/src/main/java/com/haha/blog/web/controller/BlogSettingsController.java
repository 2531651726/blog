package com.haha.blog.web.controller;


import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.common.domain.vo.BlogSettings.BlogSettingsVO;
import com.haha.blog.web.service.IBlogSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 博客设置表 前端控制器
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
@RestController
@RequestMapping("/blog/settings")
@Api(tags = "博客信息模块")
@RequiredArgsConstructor
public class BlogSettingsController {

    private final IBlogSettingsService blogSettingsService;

    @PostMapping("/detail")
    @ApiOperation("获取博客信息")
    @ApiOperationLog(description = "获取博客信息")
    public BlogSettingsVO queryBlogSettings() {
        return blogSettingsService.queryBlogSettings();
    }

}
