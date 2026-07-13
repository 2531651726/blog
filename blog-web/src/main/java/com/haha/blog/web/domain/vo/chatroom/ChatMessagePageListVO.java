package com.haha.blog.web.domain.vo.chatroom;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ChatMessagePageListVO {

    private List<ChatMessageVO> messages;
    private Boolean hasMore;
}
