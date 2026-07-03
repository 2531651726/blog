package com.haha.blog.common.domain.vo.dashboard;

import lombok.Data;

@Data
public class DashboardCategoryRelInfoVO {
    // 分类名称
    private String name;
    // 该分类下文章数量
    private Integer value;
}
