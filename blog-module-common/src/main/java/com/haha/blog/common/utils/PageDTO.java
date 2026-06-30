package com.haha.blog.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class PageDTO<T> extends Response<List<T>> {

    private long total = 0L;   // 总记录数
    private long size = 10L;   //每页显示的记录数，默认每页显示 10 条
    private long current;   //当前页码
    private long pages;   //总页数

    /**
     * 成功响应
     * @param page Mybatis Plus 提供的分页接口
     * @param data
     * @return
     * @param <T>
     */
    public static <T> PageDTO<T> success(IPage<?> page, List<T> data) {
        PageDTO<T> response = new PageDTO<>();
        response.setSuccess(true);
        response.setCurrent(Objects.isNull(page) ? 1L : page.getCurrent());
        response.setSize(Objects.isNull(page) ? 10L : page.getSize());
        response.setPages(Objects.isNull(page) ? 0L : page.getPages());
        response.setTotal(Objects.isNull(page) ? 0L : page.getTotal());
        response.setData(data);
        return response;
    }
}
