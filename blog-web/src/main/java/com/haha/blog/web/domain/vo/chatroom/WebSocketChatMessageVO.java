package com.haha.blog.web.domain.vo.chatroom;

import com.haha.blog.web.enums.ChatMessageType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebSocketChatMessageVO {
    /**
     * 消息类型
     */
    private ChatMessageType type;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private String time;

    /**
     * 在线人数
     */
    private Integer onlineCount;

    /**
     * 返回会话 ID
     */
    private String sessionId;
}
