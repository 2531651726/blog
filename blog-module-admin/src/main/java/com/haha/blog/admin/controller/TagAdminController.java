package com.haha.blog.admin.controller;

import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.admin.domain.dto.tag.AddTagDTO;
import com.haha.blog.admin.domain.dto.tag.DeleteTagDTO;
import com.haha.blog.admin.domain.dto.tag.SearchTagDTO;
import com.haha.blog.admin.domain.query.tag.TagPageQuery;
import com.haha.blog.common.domain.vo.QuerySelectListVO;
import com.haha.blog.admin.domain.vo.Tag.TagPageListVO;
import com.haha.blog.admin.service.ITagAdminService;
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
@RequestMapping("/admin/tag")
@RequiredArgsConstructor
@Api(tags = "admin标签模块")
public class TagAdminController {

    final private ITagAdminService tagService;

    @PostMapping("/add")
    @ApiOperation("新增标签")
    @ApiOperationLog(description = "新增标签")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addTags(@RequestBody @Validated AddTagDTO dto) {
        tagService.addTags(dto);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询标签")
    @ApiOperationLog(description = "分页查询标签")
    public PageDTO<TagPageListVO> queryTagPageList(@RequestBody TagPageQuery query) {
        return tagService.queryTagPageList(query);
    }

    @PostMapping("/delete")
    @ApiOperation("删除标签")
    @ApiOperationLog(description = "删除标签")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteTag(@RequestBody @Validated DeleteTagDTO dto) {
        tagService.deleteTagById(dto);
    }

    @PostMapping("/search")
    @ApiOperation("标签模糊查询")
    @ApiOperationLog(description = "标签模糊查询")
    public List<QuerySelectListVO> searchTag(@RequestBody @Validated SearchTagDTO dto) {
        return tagService.searchTags(dto);
    }

    @PostMapping("/select/list")
    @ApiOperation("获取所有标签下拉框数据")
    @ApiOperationLog(description = "获取所有标签下拉框数据")
    public List<QuerySelectListVO> queryTagSelectList() {
        return tagService.queryTagSelectList();
    }

}
