package com.haha.blog.web.controller;

import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.web.domain.query.wiki.WikiArticlePreNextQuery;
import com.haha.blog.web.domain.query.wiki.WikiCatalogQuery;
import com.haha.blog.web.domain.vo.wiki.WikiArticlePreNextVO;
import com.haha.blog.web.domain.vo.wiki.WikiCatalogVO;
import com.haha.blog.web.domain.vo.wiki.WikiVO;
import com.haha.blog.web.service.IWikiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wiki")
@Api(tags = "知识库模块")
@RequiredArgsConstructor
public class WikiController {

    private final IWikiService wikiService;

    @PostMapping("/list")
    @ApiOperation("获取知识库")
    @ApiOperationLog(description = "获取知识库")
    public List<WikiVO> queryWikiPage(){
        return wikiService.queryWikiPage();
    }

    @PostMapping("/catalog/list")
    @ApiOperation("获取知识库目录")
    @ApiOperationLog(description = "获取知识库目录")
    public List<WikiCatalogVO> queryWikiCatalog(@RequestBody @Validated WikiCatalogQuery query){
        return wikiService.queryWikiCatalog(query);
    }

    @PostMapping("/article/preNext")
    @ApiOperation("获取知识库文章上下页")
    @ApiOperationLog(description = "获取知识库文章上下页")
    public WikiArticlePreNextVO queryWikiArticlePreAndNext(@RequestBody @Validated WikiArticlePreNextQuery query){
        return wikiService.queryWikiArticlePreAndNext(query);
    }




}
