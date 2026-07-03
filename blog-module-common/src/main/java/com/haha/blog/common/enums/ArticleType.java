package com.haha.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ArticleType implements BaseEnum {
    NORMAL(1, "普通文章"),
    WIKI(2, "知识库文章"),
    ;
    @JsonValue
    @EnumValue
    final int value;
    final String desc;

    ArticleType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ArticleType of(Integer value){
        if (value == null) {
            return null;
        }
        for (ArticleType type : values()) {
            if (type.equalsValue(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("非法的文章类型：" + value);
    }

}
