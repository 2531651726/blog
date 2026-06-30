package com.haha.blog.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.haha.blog.admin.domain.vo.dashboard.DashboardPVStatisticsInfoVO;
import com.haha.blog.admin.domain.vo.dashboard.DashboardStatisticsInfoVO;
import com.haha.blog.admin.service.IDashboardAdminService;
import com.haha.blog.common.constants.DateConstants;
import com.haha.blog.common.domain.dto.dashboard.ArticlePublishCountDTO;
import com.haha.blog.common.domain.dos.StatisticsArticlePvDO;
import com.haha.blog.common.mapper.ArticleMapper;
import com.haha.blog.common.mapper.CategoryMapper;
import com.haha.blog.common.mapper.StatisticsArticlePvMapper;
import com.haha.blog.common.mapper.TagMapper;
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
}
