package com.haha.blog.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.haha.blog.admin.service.IStatisticsAdminService;
import com.haha.blog.common.domain.dto.category.CategoryAndArticlesTotalDTO;
import com.haha.blog.common.domain.dto.tag.TagAndArticlesTotalDTO;
import com.haha.blog.common.mapper.ArticleCategoryRelMapper;
import com.haha.blog.common.mapper.ArticleTagRelMapper;
import com.haha.blog.common.mapper.CategoryMapper;
import com.haha.blog.common.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsAdminServiceImpl implements IStatisticsAdminService {

    private final CategoryMapper categoryMapper;
    private final ArticleCategoryRelMapper articleCategoryRelMapper;
    private final TagMapper tagMapper;
    private final ArticleTagRelMapper articleTagRelMapper;

    @Override
    public void statisticsCategoryArticleTotal() {
        try{
            // 查询所有分类下的文章数量记录
            List<CategoryAndArticlesTotalDTO> list = articleCategoryRelMapper.selectCountGroupByCategoryId();
            if(CollectionUtil.isEmpty(list)){
                log.info("分类文章数量统计任务执行结束，未查询到任何分类数据，无需更新");
                return;
            }
            log.info("开始批量更新分类文章总数，待更新分类数量：{}", list.size());
            // 批量更新分类下文章数量记录
            int row = categoryMapper.batchUpdateArticlesTotalCount(list);
            log.info("分类文章总数批量更新完成，实际数据库受影响行数：{}", row);
        }catch (Exception e){
            log.error("分类文章数量统计任务执行异常", e);
            throw new RuntimeException("分类文章统计失败", e);
        }
    }

    @Override
    public void statisticsTagArticleTotal() {
        try{
            // 查询所有标签下的文章数量记录
            List<TagAndArticlesTotalDTO> list = articleTagRelMapper.selectCountGroupByTagId();
            if(CollectionUtil.isEmpty(list)){
                log.info("标签文章数量统计任务执行结束，未查询到任何标签数据，无需更新");
                return;
            }
            log.info("开始批量更新标签文章总数，待更新标签数量：{}", list.size());
            // 批量更新标签下文章数量记录
            int row = tagMapper.batchUpdateArticlesTotalCount(list);
            log.info("标签文章总数批量更新完成，实际数据库受影响行数：{}", row);
        }catch (Exception e){
            log.error("标签文章数量统计任务执行异常", e);
            throw new RuntimeException("标签文章统计失败", e);
        }
    }
}
