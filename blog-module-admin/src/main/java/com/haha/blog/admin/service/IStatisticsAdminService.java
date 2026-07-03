package com.haha.blog.admin.service;

import java.time.LocalDate;

public interface IStatisticsAdminService {
    /**
     * 统计各分类下文章总数
     */
    void statisticsCategoryArticleTotal();

    /**
     * 统计各标签下文章总数
     */
    void statisticsTagArticleTotal();

    void initPvRecord(LocalDate date);
}
