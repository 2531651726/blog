package com.haha.blog.admin.controller;

import com.haha.blog.common.domain.vo.dashboard.DashboardCategoryRelInfoVO;
import com.haha.blog.admin.domain.vo.dashboard.DashboardPVStatisticsInfoVO;
import com.haha.blog.admin.domain.vo.dashboard.DashboardStatisticsInfoVO;
import com.haha.blog.common.domain.vo.dashboard.DashboardTagRelInfoVO;
import com.haha.blog.admin.service.IDashboardAdminService;
import com.haha.blog.common.aspect.ApiOperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
@Api(tags = "admin仪表盘")
public class DashboardAdminController {

    private final IDashboardAdminService dashboardService;

    @PostMapping("/statistics")
    @ApiOperation(value = "获取后台仪表盘基础统计信息")
    @ApiOperationLog(description = "获取后台仪表盘基础统计信息")
    public DashboardStatisticsInfoVO queryDashboardStatistics() {
        return dashboardService.queryDashboardStatistics();
    }

    @PostMapping("/publishArticle/statistics")
    @ApiOperation(value = "获取后台仪表盘文章发布热点统计信息")
    @ApiOperationLog(description = "获取后台仪表盘文章发布热点统计信息")
    public Map<LocalDate,Long> queryArticlePublishCountByDate(){
        return dashboardService.queryArticlePublishCountByDate();
    }

    @PostMapping("/pv/statistics")
    @ApiOperation(value = "获取后台仪表盘最近一周 PV 访问量信息")
    @ApiOperationLog(description = "获取后台仪表盘最近一周 PV 访问量信息")
    public DashboardPVStatisticsInfoVO queryDashboardPVStatistics(){
        return dashboardService.queryDashboardPVStatistics();
    }

    @PostMapping("/category/statistics")
    @ApiOperation(value = "获取后台仪表盘文章分类统计信息")
    @ApiOperationLog(description = "获取后台仪表盘文章分类统计信息")
    public List<DashboardCategoryRelInfoVO> queryDashboardCategoryStatistics(){
        return dashboardService.queryDashboardCategoryStatistics();
    }

    @PostMapping("/tag/statistics")
    @ApiOperation(value = "获取后台仪表盘文章标签统计信息")
    @ApiOperationLog(description = "获取后台仪表盘文章标签统计信息")
    public DashboardTagRelInfoVO queryDashboardTagStatistics(){
        return dashboardService.queryDashboardTagStatistics();
    }

}
