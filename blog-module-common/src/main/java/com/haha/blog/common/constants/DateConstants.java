package com.haha.blog.common.constants;

import java.time.format.DateTimeFormatter;

public interface DateConstants {
    /**
     * 月-日
     */
    DateTimeFormatter MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");
    /**
     * 年-月-日 小时-分钟-秒
     */
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 小时-分钟-秒
     */
    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
}
