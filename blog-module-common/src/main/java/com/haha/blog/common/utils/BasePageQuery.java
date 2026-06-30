package com.haha.blog.common.utils;

import lombok.Data;

@Data
public class BasePageQuery {

    private Long current = 1L;  // 当前页码数
    private Long size = 10L; //每页展示的数据数量，默认每页展示 10 条数据
}
