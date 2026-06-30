package com.haha.blog.admin.service;

import com.haha.blog.admin.domain.vo.dashboard.DashboardPVStatisticsInfoVO;
import com.haha.blog.admin.domain.vo.dashboard.DashboardStatisticsInfoVO;

import java.time.LocalDate;
import java.util.Map;

public interface IDashboardAdminService {
    DashboardStatisticsInfoVO queryDashboardStatistics();

    Map<LocalDate,Long> queryArticlePublishCountByDate();

    DashboardPVStatisticsInfoVO queryDashboardPVStatistics();
}
