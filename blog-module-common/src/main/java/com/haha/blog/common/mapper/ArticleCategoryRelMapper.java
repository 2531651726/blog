package com.haha.blog.common.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.haha.blog.common.domain.dos.ArticleCategoryRelDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haha.blog.common.domain.dto.category.CategoryAndArticlesTotalDTO;
import com.haha.blog.common.domain.vo.dashboard.DashboardCategoryRelInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 文章所属分类关联表 Mapper 接口
 * </p>
 *
 * @author li
 * @since 2026-06-20
 */
@Mapper
public interface ArticleCategoryRelMapper extends BaseMapper<ArticleCategoryRelDO> {

    /**
     * 根据分类 ID 查询所有的关联记录
     * @param categoryId
     * @return
     */
    default List<ArticleCategoryRelDO> selectListByCategoryId(Long categoryId) {
        return selectList(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO::getCategoryId, categoryId));
    }

    /**
     * 根据文章 ID 查询关联记录
     * @param articleId
     * @return
     */
    default ArticleCategoryRelDO selectByArticleId(Long articleId){
        return selectOne(Wrappers.<ArticleCategoryRelDO>lambdaQuery()
                .eq(ArticleCategoryRelDO::getArticleId, articleId));
    }

    /**
     * 统计每个分类下关联的文章数量
     */
    List<CategoryAndArticlesTotalDTO> selectCountGroupByCategoryId();


}
