package com.haha.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PublishStatus implements BaseEnum{
    UN_PUBLISH(0, "未发布"),
    PUBLISHED(1, "已发布");

    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    PublishStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PublishStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (PublishStatus status : values()) {
            if (status.equalsValue(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("非法的发布状态：" + value);
    }
}
