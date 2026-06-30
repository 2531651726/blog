package com.haha.blog.web.controller;


import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.category.CategoryArticlePageQuery;
import com.haha.blog.web.domain.vo.category.CategoryArticleVO;
import com.haha.blog.web.domain.vo.category.CategoryVO;
import com.haha.blog.web.service.ICategoryService;
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
 * 文章分类表 前端控制器
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
@RestController
@RequestMapping("/category")
@Api(tags = "分类模块")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @PostMapping("/list")
    @ApiOperation("获取分类列表")
    @ApiOperationLog(description = "获取分类列表")
    public List<CategoryVO> queryCategoryList() {
        return categoryService.queryCategoryList();
    }

    @PostMapping("/article/list")
    @ApiOperation("获取某个分类下的文章列表")
    @ApiOperationLog(description = "获取某个分类下的文章列表")
    public PageDTO<CategoryArticleVO> queryArticleByCategoryPage(@RequestBody @Validated CategoryArticlePageQuery query){
        return categoryService.queryArticleByCategoryPage(query);
    }

}
