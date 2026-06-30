package com.haha.blog.common.utils;

public interface Convert<R,T> {
    void convert(R origin, T target);
}
