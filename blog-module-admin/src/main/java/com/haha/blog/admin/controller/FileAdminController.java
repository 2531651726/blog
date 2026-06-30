package com.haha.blog.admin.controller;

import com.haha.blog.admin.domain.vo.file.FileUploadVO;
import com.haha.blog.admin.service.IFileAdminService;
import com.haha.blog.common.aspect.ApiOperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin")
@Api(tags = "admin文件模块")
@RequiredArgsConstructor
public class FileAdminController {

    private final IFileAdminService fileservice;

    @PostMapping("/file/upload")
    @ApiOperation("上传文件")
    @ApiOperationLog(description = "上传文件")
    public FileUploadVO uploadFile(MultipartFile file) {
        return fileservice.uploadFile(file);
    }
}
