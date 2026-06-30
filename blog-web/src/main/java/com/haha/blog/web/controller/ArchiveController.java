package com.haha.blog.web.controller;

import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.archive.ArchiveArticlePageQuery;
import com.haha.blog.web.domain.vo.archive.ArchiveArticlePageVO;
import com.haha.blog.web.service.IArchiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/archive")
@Api(tags = "归档模块")
@RequiredArgsConstructor
public class ArchiveController {

    private final IArchiveService archiveService;

    @PostMapping("/list")
    @ApiOperation("文章归档分页查询")
    @ApiOperationLog(description = "文章归档分页查询")
    public PageDTO<ArchiveArticlePageVO> queryArchiveArticlePage(@RequestBody ArchiveArticlePageQuery query){
        return archiveService.queryArchiveArticlePage(query);
    }


}
