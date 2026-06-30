package com.haha.blog.admin.event.message;

import org.springframework.context.ApplicationEvent;

public class ArticleChangeMessage extends ApplicationEvent {

    public ArticleChangeMessage(Object source) {
        super(source);
    }
}
