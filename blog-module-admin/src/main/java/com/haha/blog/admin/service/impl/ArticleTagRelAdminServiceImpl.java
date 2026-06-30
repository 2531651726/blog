package com.haha.blog.admin.service.impl;

import com.haha.blog.admin.service.IArticleTagRelAdminService;
import com.haha.blog.common.domain.dos.ArticleTagRelDO;
import com.haha.blog.common.mapper.ArticleTagRelMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章对应标签关联表 服务实现类
 * </p>
 *
 * @author li
 * @since 2026-06-20
 */
@Service
public class ArticleTagRelAdminServiceImpl extends ServiceImpl<ArticleTagRelMapper, ArticleTagRelDO> implements IArticleTagRelAdminService {

}
