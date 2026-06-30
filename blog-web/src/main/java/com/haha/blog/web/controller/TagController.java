package com.haha.blog.web.controller;


import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.category.CategoryArticlePageQuery;
import com.haha.blog.web.domain.query.tag.TagArticlePageQuery;
import com.haha.blog.web.domain.vo.tag.TagArticleVO;
import com.haha.blog.web.domain.vo.tag.TagVO;
import com.haha.blog.web.service.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 文章标签表 前端控制器
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
@RestController
@RequestMapping("/tag")
@Api(tags = "标签模块")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @PostMapping("/list")
    @ApiOperation("查询标签列表")
    @ApiOperationLog(description = "查询标签列表")
    public List<TagVO> queryTagList(){
        return tagService.queryTagList();
    }

    @PostMapping("/article/list")
    @ApiOperation("查询某个标签下的文章列表")
    @ApiOperationLog(description = "查询某个标签下的文章列表")
    public PageDTO<TagArticleVO> queryArticleByTagPage(@RequestBody @Validated TagArticlePageQuery query){
        return tagService.queryArticleByTagPage(query);
    }

}
