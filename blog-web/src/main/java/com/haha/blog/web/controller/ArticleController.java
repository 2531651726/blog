package com.haha.blog.web.controller;


import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.article.ArticleDetailQuery;
import com.haha.blog.web.domain.query.article.ArticlePageQuery;
import com.haha.blog.web.domain.vo.article.ArticleDetailVO;
import com.haha.blog.web.domain.vo.article.ArticleVO;
import com.haha.blog.web.service.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@Api(tags = "文章模块")
public class ArticleController {

    private final IArticleService articleService;

    @PostMapping("/list")
    @ApiOperation("分页查询文章")
    @ApiOperationLog(description = "分页查询文章")
    public PageDTO<ArticleVO> queryArticlePage(@RequestBody ArticlePageQuery query){
        return articleService.queryArticlePage(query);
    }

    @PostMapping("/detail")
    @ApiOperation("查询文章详情")
    @ApiOperationLog(description = "查询文章详情")
    public ArticleDetailVO queryArticleDetail(@RequestBody @Validated ArticleDetailQuery query){
        return articleService.queryArticleDetail(query);
    }

}
