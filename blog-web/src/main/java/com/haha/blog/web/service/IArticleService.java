package com.haha.blog.web.service;

import com.haha.blog.common.domain.dos.ArticleDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.article.ArticleDetailQuery;
import com.haha.blog.web.domain.query.article.ArticlePageQuery;
import com.haha.blog.web.domain.vo.article.ArticleDetailVO;
import com.haha.blog.web.domain.vo.article.ArticleVO;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
public interface IArticleService extends IService<ArticleDO> {

    PageDTO<ArticleVO> queryArticlePage(ArticlePageQuery query);

    ArticleDetailVO queryArticleDetail(ArticleDetailQuery query);
}
