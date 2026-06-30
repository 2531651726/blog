package com.haha.blog.admin.service.impl;

import com.haha.blog.admin.service.IArticleCategoryRelAdminService;
import com.haha.blog.common.domain.dos.ArticleCategoryRelDO;
import com.haha.blog.common.mapper.ArticleCategoryRelMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章所属分类关联表 服务实现类
 * </p>
 *
 * @author li
 * @since 2026-06-20
 */
@Service
public class ArticleCategoryRelAdminServiceImpl extends ServiceImpl<ArticleCategoryRelMapper, ArticleCategoryRelDO> implements IArticleCategoryRelAdminService {

}
