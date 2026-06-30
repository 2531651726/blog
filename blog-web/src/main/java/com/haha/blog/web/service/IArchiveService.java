package com.haha.blog.web.service;

import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.archive.ArchiveArticlePageQuery;
import com.haha.blog.web.domain.vo.archive.ArchiveArticlePageVO;

import java.util.List;

public interface IArchiveService {
    PageDTO<ArchiveArticlePageVO> queryArchiveArticlePage(ArchiveArticlePageQuery query);
}
