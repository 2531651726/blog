package com.haha.blog.web.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContext.context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        if (context == null) {
            throw new RuntimeException("Spring上下文尚未初始化");
        }
        return context.getBean(beanClass);
    }


}
