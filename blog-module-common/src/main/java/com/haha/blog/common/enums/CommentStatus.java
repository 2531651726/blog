package com.haha.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CommentStatus implements BaseEnum{

    PENDING_AUDIT(1, "待审核"),
    NORMAL(2, "正常"),
    AUDIT_REJECT(3, "审核未通过");

    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    CommentStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CommentStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (CommentStatus status : values()) {
            if (status.equalsValue(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("非法的发布状态：" + value);
    }
}
