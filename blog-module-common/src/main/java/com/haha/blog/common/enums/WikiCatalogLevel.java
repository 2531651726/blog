package com.haha.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum WikiCatalogLevel implements BaseEnum{
    FIRST_LEVEL(1, "一级目录"),
    SECOND_LEVEL(2, "二级目录");
    ;
    @JsonValue
    @EnumValue
    final int value;
    final String desc;

    WikiCatalogLevel(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static WikiCatalogLevel of(Integer value){
        if (value == null) {
            return null;
        }
        for (WikiCatalogLevel level : values()) {
            if (level.equalsValue(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("非法的目录层级：" + value);
    }
}
