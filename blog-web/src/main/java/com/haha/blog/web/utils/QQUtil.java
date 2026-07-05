package com.haha.blog.web.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class QQUtil {

    private static final Pattern QQ_PATTERN = Pattern.compile("^[1-9]\\d{4,12}$");

    /**
     * 简单校验QQ格式
     */
    public static boolean isLegalQQ(String qqStr) {
        if (qqStr == null || StringUtils.isBlank(qqStr)) {
            return false;
        }
        return QQ_PATTERN.matcher(qqStr).matches();
    }
}
