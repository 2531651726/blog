package com.haha.blog.web.service.impl;

import com.haha.blog.admin.domain.vo.dashboard.DashboardStatisticsInfoVO;
import com.haha.blog.common.mapper.ArticleMapper;
import com.haha.blog.common.mapper.CategoryMapper;
import com.haha.blog.common.mapper.TagMapper;
import com.haha.blog.web.domain.vo.statistics.StatisticsInfoVO;
import com.haha.blog.web.service.IStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements IStatisticsService {

    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;

    @Override
    public StatisticsInfoVO queryStatisticsInfo() {
        // 文章总数
        Long articleCount = articleMapper.selectCount(null);
        // 分类总数
        Long categoryCount = categoryMapper.selectCount(null);
        // 标签总数
        Long tagCount = tagMapper.selectCount(null);
        // 总浏览量
        Long pvTotalCount = articleMapper.queryTotalViewCount();
        return new StatisticsInfoVO()
                .setArticleTotalCount(articleCount)
                .setCategoryTotalCount(categoryCount)
                .setTagTotalCount(tagCount)
                .setPvTotalCount(pvTotalCount);
    }
}
