package com.haha.blog.admin.domain.vo.dashboard;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "查询仪表盘文章 PV 访问量信息出参 VO")
public class DashboardPVStatisticsInfoVO {
    /**
     * 日期集合
     */
    private List<String> pvDates;

    /**
     * PV 浏览量集合
     */
    private List<Long> pvCounts;
}
