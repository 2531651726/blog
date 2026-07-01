package com.haha.blog.common.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD) // 仅作用在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 忽略全局响应封装，原样返回
public @interface SkipResponseWrap {
}
