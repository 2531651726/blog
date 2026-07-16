package com.haha.blog.web.service;

import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.dto.search.SearchArticlePageListDTO;
import com.haha.blog.web.domain.vo.search.SearchArticlePageListVO;

public interface ISearchService {
    PageDTO<SearchArticlePageListVO> searchArticlePageList(SearchArticlePageListDTO dto);
}
