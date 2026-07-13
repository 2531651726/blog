package com.haha.blog.web.domain.query.chatroom;

import lombok.Data;

@Data
public class ChatMessagePageListQuery {
    private Long lastId;
    private String sessionId;
}
