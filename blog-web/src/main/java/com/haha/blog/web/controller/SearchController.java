package com.haha.blog.web.controller;

import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.dto.search.SearchArticlePageListDTO;
import com.haha.blog.web.domain.vo.search.SearchArticlePageListVO;
import com.haha.blog.web.service.ISearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "搜索")
@RequiredArgsConstructor
public class SearchController {

    private final ISearchService searchService;

    @PostMapping("/article/search")
    @ApiOperation(value = "文章搜索")
    @ApiOperationLog(description = "文章搜索")
    public PageDTO<SearchArticlePageListVO> searchArticlePageList(@RequestBody @Validated SearchArticlePageListDTO dto) {
        return searchService.searchArticlePageList(dto);
    }
}
