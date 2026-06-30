package com.haha.blog.admin.service.impl;

import com.haha.blog.admin.service.IArticleContentAdminService;
import com.haha.blog.common.domain.dos.ArticleContentDO;
import com.haha.blog.common.mapper.ArticleContentMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章内容表 服务实现类
 * </p>
 *
 * @author li
 * @since 2026-06-20
 */
@Service
public class ArticleContentAdminServiceImpl extends ServiceImpl<ArticleContentMapper, ArticleContentDO> implements IArticleContentAdminService {

}
