package com.haha.blog.web.domain.vo.chatroom;

import com.haha.blog.web.enums.ChatMessageType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OnlineUsersMessageVO {
    private ChatMessageType type;
    private List<OnlineUserVO> users;
    private Integer onlineCount;
}
