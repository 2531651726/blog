package com.haha.blog.web.event.message;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommentEvent extends ApplicationEvent {

    private final Long commentId;

    public CommentEvent(Object source, Long commentId) {
        super(source);
        this.commentId = commentId;
    }
}
