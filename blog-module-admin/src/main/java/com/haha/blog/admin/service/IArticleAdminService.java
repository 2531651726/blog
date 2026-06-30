package com.haha.blog.admin.service;

import com.haha.blog.common.domain.dos.ArticleDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.admin.domain.dto.Article.DeleteArticleDTO;
import com.haha.blog.admin.domain.dto.Article.UpdateArticleDTO;
import com.haha.blog.admin.domain.query.article.ArticleDetailQuery;
import com.haha.blog.admin.domain.query.article.ArticlePageListQuery;
import com.haha.blog.admin.domain.query.article.PublishArticleQuery;
import com.haha.blog.admin.domain.vo.article.ArticleDetailVO;
import com.haha.blog.admin.domain.vo.article.ArticlePageListVO;
import com.haha.blog.common.utils.PageDTO;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author li
 * @since 2026-06-20
 */
public interface IArticleAdminService extends IService<ArticleDO> {

    void publishArticle(PublishArticleQuery query);

    void deleteArticle(DeleteArticleDTO dto);

    PageDTO<ArticlePageListVO> queryArticlePageList(ArticlePageListQuery query);

    ArticleDetailVO queryArticleDetail(ArticleDetailQuery query);

    void updateArticle(UpdateArticleDTO dto);
}
