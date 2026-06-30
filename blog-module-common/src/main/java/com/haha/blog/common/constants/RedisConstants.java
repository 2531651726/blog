package com.haha.blog.common.constants;

public interface RedisConstants {

    /**
     * 文章阅读量增量 Hash key
     * hashKey: 文章ID, value: 自上次同步以来的新增阅读量
     */
    String ARTICLE_VIEW_COUNT_DELTA_KEY = "article:view:count:delta";

    /**
     * 文章总浏览量基准值 key
     * value: 上次同步时 SUM(read_num) 的快照值, 用于计算本次增量 PV
     */
    String ARTICLE_TOTAL_VIEW_COUNT_KEY = "article:total:view:count";
}
