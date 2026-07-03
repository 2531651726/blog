package com.haha.blog.admin.controller;

import com.haha.blog.admin.domain.dto.wiki.*;
import com.haha.blog.admin.domain.query.wiki.WikiCatalogListQuery;
import com.haha.blog.admin.domain.query.wiki.WikiPageListQuery;
import com.haha.blog.admin.domain.vo.wiki.WikiCatalogListVO;
import com.haha.blog.admin.domain.vo.wiki.WikiPageListVO;
import com.haha.blog.admin.service.IWikiAdminService;
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

import java.util.List;

@RestController
@RequestMapping("/admin/wiki")
@Api(tags = "admin知识库模块")
@RequiredArgsConstructor
public class WikiAdminController {

    private final IWikiAdminService wikiService;

    @PostMapping("/add")
    @ApiOperation("新增知识库")
    @ApiOperationLog(description = "新增知识库")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void insertWiki(@RequestBody @Validated InsertWikiDTO dto){
        wikiService.insertWiki(dto);
    }

    @PostMapping("/delete")
    @ApiOperation("删除知识库")
    @ApiOperationLog(description = "删除知识库")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteWiki(@RequestBody @Validated DeleteWikiDTO dto){
        wikiService.deleteWiki(dto);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询知识库")
    @ApiOperationLog(description = "分页查询知识库")
    public PageDTO<WikiPageListVO> queryWikiPageList(@RequestBody WikiPageListQuery query){
        return wikiService.queryWikiPageList(query);
    }

    @PostMapping("/isTop/update")
    @ApiOperation("更新知识库置顶状态")
    @ApiOperationLog(description = "更新知识库置顶状态")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateWikiWeight(@Validated @RequestBody UpdateWikiWeightDTO dto){
        wikiService.updateWikiWeight(dto);
    }

    @PostMapping("/isPublish/update")
    @ApiOperation("更新知识库发布状态")
    @ApiOperationLog(description = "更新知识库发布状态")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateWikiPublishStatus(@Validated @RequestBody UpdateWikiPublishStatusDTO dto){
        wikiService.updateWikiPublishStatus(dto);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新知识库")
    @ApiOperationLog(description = "更新知识库")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateWiki(@RequestBody @Validated UpdateWikiDTO dto) {
        wikiService.updateWiki(dto);
    }

    @PostMapping("/catalog/list")
    @ApiOperation(value = "查询知识库目录数据")
    @ApiOperationLog(description = "查询知识库目录数据")
    public List<WikiCatalogListVO> queryWikiCatalogList(@RequestBody @Validated WikiCatalogListQuery query) {
        return wikiService.queryWikiCatalogList(query);
    }

    @PostMapping("/catalog/update")
    @ApiOperation(value = "更新知识库目录")
    @ApiOperationLog(description = "更新知识库目录")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateWikiCatalogs(@RequestBody @Validated UpdateWikiCatalogDTO dto) {
        wikiService.updateWikiCatalogs(dto);
    }



}
