package com.haha.blog.admin.controller;

import com.haha.blog.admin.domain.dto.article.UpdateArticlePublishStatusDTO;
import com.haha.blog.admin.domain.dto.article.UpdateArticleWeightDTO;
import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.admin.domain.dto.article.DeleteArticleDTO;
import com.haha.blog.admin.domain.dto.article.UpdateArticleDTO;
import com.haha.blog.admin.domain.query.article.ArticleDetailQuery;
import com.haha.blog.admin.domain.query.article.ArticlePageListQuery;
import com.haha.blog.admin.domain.query.article.PublishArticleQuery;
import com.haha.blog.admin.domain.vo.article.ArticleDetailVO;
import com.haha.blog.admin.domain.vo.article.ArticlePageListVO;
import com.haha.blog.admin.service.IArticleAdminService;
import com.haha.blog.common.utils.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/article")
@Api(tags = "admin文章模块")
@RequiredArgsConstructor
public class ArticleAdminController {

    private final IArticleAdminService articleService;

    @PostMapping("/publish")
    @ApiOperation("发布文章")
    @ApiOperationLog(description = "发布文章")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void publishArticle(@Validated @RequestBody PublishArticleQuery query){
        articleService.publishArticle(query);
    }

    @PostMapping("/delete")
    @ApiOperation("删除文章")
    @ApiOperationLog(description = "删除文章")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteArticle(@Validated @RequestBody DeleteArticleDTO dto){
        articleService.deleteArticle(dto);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询文章")
    @ApiOperationLog(description = "分页查询文章")
    public PageDTO<ArticlePageListVO> queryArticlePageList(@Validated @RequestBody ArticlePageListQuery query){
        return articleService.queryArticlePageList(query);
    }

    @PostMapping("/detail")
    @ApiOperation("查询文章详情")
    @ApiOperationLog(description = "查询文章详情")
    public ArticleDetailVO queryArticleDetail(@Validated @RequestBody ArticleDetailQuery query){
        return articleService.queryArticleDetail(query);
    }

    @PostMapping("/update")
    @ApiOperation("更新文章")
    @ApiOperationLog(description = "更新文章")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateArticle(@Validated @RequestBody UpdateArticleDTO dto){
        articleService.updateArticle(dto);
    }

    @PostMapping("/isTop/update")
    @ApiOperation(value = "更新文章置顶状态")
    @ApiOperationLog(description = "更新文章置顶状态")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateArticleWeight(@Validated @RequestBody UpdateArticleWeightDTO dto){
        articleService.updateArticleWeight(dto);
    }

    @PostMapping("/isPublish/update")
    @ApiOperation(value = "更新文章发布状态")
    @ApiOperationLog(description = "更新文章发布状态")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateArticlePublishStatus(@Validated @RequestBody UpdateArticlePublishStatusDTO dto){
        articleService.updateArticlePublishStatus(dto);
    }
}
