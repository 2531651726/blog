package com.haha.blog.admin.service;

import com.haha.blog.common.domain.vo.dashboard.DashboardCategoryRelInfoVO;
import com.haha.blog.admin.domain.vo.dashboard.DashboardPVStatisticsInfoVO;
import com.haha.blog.admin.domain.vo.dashboard.DashboardStatisticsInfoVO;
import com.haha.blog.common.domain.vo.dashboard.DashboardTagRelInfoVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IDashboardAdminService {
    DashboardStatisticsInfoVO queryDashboardStatistics();

    Map<LocalDate,Long> queryArticlePublishCountByDate();

    DashboardPVStatisticsInfoVO queryDashboardPVStatistics();

    List<DashboardCategoryRelInfoVO> queryDashboardCategoryStatistics();

    DashboardTagRelInfoVO queryDashboardTagStatistics();
}
