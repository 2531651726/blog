package com.haha.blog.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haha.blog.common.domain.dos.ArticleCategoryRelDO;
import com.haha.blog.common.domain.dos.ArticleDO;
import com.haha.blog.common.domain.dos.CategoryDO;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.ArticleCategoryRelMapper;
import com.haha.blog.common.mapper.ArticleMapper;
import com.haha.blog.common.mapper.CategoryMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.category.CategoryArticlePageQuery;
import com.haha.blog.web.domain.vo.category.CategoryArticleVO;
import com.haha.blog.web.domain.vo.category.CategoryVO;
import com.haha.blog.web.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章分类表 服务实现类
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements ICategoryService {

    private final CategoryMapper categoryMapper;
    private final ArticleCategoryRelMapper articleCategoryRelMapper;
    private final ArticleMapper articleMapper;

    @Override
    public List<CategoryVO> queryCategoryList() {
        List<CategoryDO> dos = lambdaQuery().list();
        List<CategoryVO> list = null;
        if(CollectionUtil.isNotEmpty(dos)){
            list = dos.stream()
                    .map(DO -> new CategoryVO()
                            .setId(DO.getId())
                            .setName(DO.getName())
                            .setArticlesTotal(DO.getArticlesTotal()))
                    .collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public PageDTO<CategoryArticleVO> queryArticleByCategoryPage(CategoryArticlePageQuery query) {
        Long current = query.getCurrent();
        Long size = query.getSize();
        Long categoryId = query.getId();
        // 查询分类
        CategoryDO categoryDO = lambdaQuery().eq(CategoryDO::getId, categoryId).one();
        if(ObjectUtil.isEmpty(categoryDO)){
            log.warn("==> 该分类不存在, categoryId: {}", categoryId);
            throw new BizException("该分类不存在");
        }
        // 查询分类下的文章
        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelMapper.selectListByCategoryId(categoryId);
        if(CollectionUtil.isEmpty(articleCategoryRelDOS)){
            log.info("==> 该分类下没有文章, categoryId: {}", categoryId);
            return PageDTO.success(null,null);
        }
        // 获取文章id集合
        Set<Long> articleIds = articleCategoryRelDOS.stream().map(ArticleCategoryRelDO::getArticleId).collect(Collectors.toSet());
        // 获取文章DO集合
        Page<ArticleDO> page = articleMapper.selectPageListByArticleIds(current, size, articleIds);
        List<ArticleDO> records = page.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return PageDTO.success(page,Collections.emptyList());
        }
        // DO 转 VO
        List<CategoryArticleVO> list = records.stream()
                .map(vo -> {
                    CategoryArticleVO categoryArticleVO = BeanUtils.copyBean(vo, CategoryArticleVO.class);
                    categoryArticleVO.setCreateDate(LocalDate.from(vo.getCreateTime()));
                    return categoryArticleVO;
                })
                .collect(Collectors.toList());
        return PageDTO.success(page,list);
    }
}
