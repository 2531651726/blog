package com.haha.blog.web.domain.vo.chatroom;

import com.haha.blog.web.enums.ChatMessageType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatMessageVO {

    private Long id;
    private String nickname;
    private String avatar;
    private String content;
    private String time;
    private ChatMessageType type;

    private Boolean isSelf;
}
