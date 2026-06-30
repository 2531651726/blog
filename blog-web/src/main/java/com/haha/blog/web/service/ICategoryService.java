package com.haha.blog.web.service;

import com.haha.blog.common.domain.dos.CategoryDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.category.CategoryArticlePageQuery;
import com.haha.blog.web.domain.vo.category.CategoryArticleVO;
import com.haha.blog.web.domain.vo.category.CategoryVO;

import java.util.List;

/**
 * <p>
 * 文章分类表 服务类
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
public interface ICategoryService extends IService<CategoryDO> {

    List<CategoryVO> queryCategoryList();

    PageDTO<CategoryArticleVO> queryArticleByCategoryPage(CategoryArticlePageQuery query);
}
