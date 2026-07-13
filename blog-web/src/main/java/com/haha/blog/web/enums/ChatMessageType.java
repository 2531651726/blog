package com.haha.blog.web.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.haha.blog.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum ChatMessageType implements BaseEnum {
    SYSTEM(0, "系统消息"),
    CHAT(1, "聊天消息"),
    ONLINE_USERS(2, "在线用户列表消息"),
    INIT(3, "会话初始化消息"), // 用于返回 sessionId
    ;
    @JsonValue
    @EnumValue
    final int value;
    final String desc;

    ChatMessageType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ChatMessageType of(Integer value){
        if (value == null) {
            return null;
        }
        for (ChatMessageType type : values()) {
            if (type.equalsValue(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("非法的消息类型：" + value);
    }
}
