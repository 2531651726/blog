package com.haha.blog.common.domain.vo.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class DashboardTagRelInfoVO {
    // 标签名字集合
    private List<String> tagNames;
    // 该标签下的文章数量集合
    private List<Integer> articleCounts;
}
