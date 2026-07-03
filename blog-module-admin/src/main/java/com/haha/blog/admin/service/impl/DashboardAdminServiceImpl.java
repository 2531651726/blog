package com.haha.blog.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.haha.blog.common.domain.dos.CategoryDO;
import com.haha.blog.common.domain.dos.TagDO;
import com.haha.blog.common.domain.vo.dashboard.DashboardCategoryRelInfoVO;
import com.haha.blog.admin.domain.vo.dashboard.DashboardPVStatisticsInfoVO;
import com.haha.blog.admin.domain.vo.dashboard.DashboardStatisticsInfoVO;
import com.haha.blog.common.domain.vo.dashboard.DashboardTagRelInfoVO;
import com.haha.blog.admin.service.IDashboardAdminService;
import com.haha.blog.common.constants.DateConstants;
import com.haha.blog.common.domain.dto.dashboard.ArticlePublishCountDTO;
import com.haha.blog.common.domain.dos.StatisticsArticlePvDO;
import com.haha.blog.common.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DashboardAdminServiceImpl implements IDashboardAdminService {

    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final StatisticsArticlePvMapper articlePvMapper;

    @Override
    public DashboardStatisticsInfoVO queryDashboardStatistics() {
        // 文章总数
        Long articleCount = articleMapper.selectCount(null);
        // 分类总数
        Long categoryCount = categoryMapper.selectCount(null);
        // 标签总数
        Long tagCount = tagMapper.selectCount(null);
        // 总浏览量
        Long pvTotalCount = articleMapper.queryTotalViewCount();
        return new DashboardStatisticsInfoVO()
                .setArticleTotalCount(articleCount)
                .setCategoryTotalCount(categoryCount)
                .setTagTotalCount(tagCount)
                .setPvTotalCount(pvTotalCount);
    }

    @Override
    public Map<LocalDate,Long> queryArticlePublishCountByDate() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);
        LinkedHashMap<LocalDate, Long> resultMap = new LinkedHashMap<>();
        // 查询一周内每日发布文章数量
        List<ArticlePublishCountDTO> articlePublishCountDTOS = articleMapper.queryArticlePublishCountByDate(startDate, endDate);
        if(!CollectionUtil.isEmpty(articlePublishCountDTOS)){
            // 封装map
            TreeMap<LocalDate, Long> map = articlePublishCountDTOS.stream()
                    .collect(Collectors.toMap(
                            ArticlePublishCountDTO::getDate,
                            ArticlePublishCountDTO::getCount,
                            Long::sum, // 同一天多条记录累加，防止重复key报错
                            TreeMap::new
                    ));
            LocalDate tempDate = startDate;
            while (!tempDate.isAfter(endDate)) {
                resultMap.put(tempDate, map.getOrDefault(tempDate, 0L));
                tempDate = tempDate.plusDays(1);
            }
        }
        return resultMap;
    }

    @Override
    public DashboardPVStatisticsInfoVO queryDashboardPVStatistics() {
        // 查询最近 7 天内的 pv 数据
        LocalDate currDate = LocalDate.now();
        List<StatisticsArticlePvDO> articlePvDOS = articlePvMapper.queryLatestWeekPvRecords(currDate);
        Map<LocalDate, Long> pvMap = new HashMap<>();
        if(!CollectionUtil.isEmpty(articlePvDOS)){
            pvMap = articlePvDOS.stream()
                    .collect(Collectors.toMap(StatisticsArticlePvDO::getPvDate, StatisticsArticlePvDO::getPvCount));
        }
        DashboardPVStatisticsInfoVO vo = null;
        // 日期集合
        List<String> pvDates = Lists.newArrayList();
        // PV 集合
        List<Long> pvCounts = Lists.newArrayList();
        // 一周前日期
        LocalDate tmpDate = currDate.minusDays(6);
        // 从一周前开始循环
        for (; tmpDate.isBefore(currDate) || tmpDate.isEqual(currDate); tmpDate = tmpDate.plusDays(1)) {
            // 设置对应日期的 PV 访问量
            pvDates.add(tmpDate.format(DateConstants.MONTH_DAY_FORMATTER));
            Long pvCount = pvMap.get(tmpDate);
            pvCounts.add(Objects.isNull(pvCount) ? 0 : pvCount);
        }
        // 封装 vo
        vo = new DashboardPVStatisticsInfoVO()
                .setPvDates(pvDates)
                .setPvCounts(pvCounts);
        return vo;
    }

    @Override
    public List<DashboardCategoryRelInfoVO> queryDashboardCategoryStatistics() {
        // 查询所有未删除的分类及其文章总数
        List<CategoryDO> categoryDOS = categoryMapper.selectList(Wrappers.<CategoryDO>lambdaQuery()
                .orderByDesc(CategoryDO::getArticlesTotal));
        if (CollectionUtil.isEmpty(categoryDOS)) {
            return Collections.emptyList();
        }
        // 转换为 VO
        return categoryDOS.stream()
                .map(category -> {
                    DashboardCategoryRelInfoVO vo = new DashboardCategoryRelInfoVO();
                    vo.setName(category.getName());
                    vo.setValue(category.getArticlesTotal() == null ? 0 : category.getArticlesTotal());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public DashboardTagRelInfoVO queryDashboardTagStatistics() {
        // 查询所有未删除的标签及其文章总数，按文章数倒序取前10
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.<TagDO>lambdaQuery()
                .orderByDesc(TagDO::getArticlesTotal)
                .last("LIMIT 10"));
        DashboardTagRelInfoVO vo = new DashboardTagRelInfoVO();
        if (CollectionUtil.isEmpty(tagDOS)) {
            vo.setTagNames(Collections.emptyList());
            vo.setArticleCounts(Collections.emptyList());
            return vo;
        }
        // 提取标签名和文章数集合
        List<String> tagNames = tagDOS.stream()
                .map(TagDO::getName)
                .collect(Collectors.toList());
        List<Integer> articleCounts = tagDOS.stream()
                .map(tag -> tag.getArticlesTotal() == null ? 0 : tag.getArticlesTotal())
                .collect(Collectors.toList());
        // 封装 VO
        vo.setTagNames(tagNames);
        vo.setArticleCounts(articleCounts);
        return vo;
    }
}
