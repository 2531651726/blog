package com.haha.blog.common.aspect;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperationLog {
    String description() default "";
}
