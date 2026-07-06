package com.haha.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum NoticeStatus implements BaseEnum{
    HIDE(0, "不展示"),
    SHOW(1, "展示");

    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    NoticeStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static NoticeStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (NoticeStatus status : values()) {
            if (status.equalsValue(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("非法的公告状态：" + value);
    }
}
