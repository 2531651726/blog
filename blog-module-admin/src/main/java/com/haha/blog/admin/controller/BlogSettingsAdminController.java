package com.haha.blog.admin.controller;

import com.haha.blog.admin.service.IBlogSettingsAdminService;
import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.admin.domain.dto.blogSettings.UpdateBlogSettingsDTO;
import com.haha.blog.common.domain.vo.BlogSettings.BlogSettingsVO;
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
@RequestMapping("/admin/blog/settings")
@Api(tags = "admin博客设置")
@RequiredArgsConstructor
public class BlogSettingsAdminController {

    private final IBlogSettingsAdminService blogSettingsService;

    @PostMapping("/update")
    @ApiOperation(value = "博客基础信息修改")
    @ApiOperationLog(description = "博客基础信息修改")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateBlogSettings(@RequestBody @Validated UpdateBlogSettingsDTO dto) {
        blogSettingsService.updateBlogSettings(dto);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "获取博客设置详情")
    @ApiOperationLog(description = "获取博客设置详情")
    public BlogSettingsVO findDetail() {
        return blogSettingsService.findDetail();
    }

}
