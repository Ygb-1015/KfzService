package com.order.main.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文件处理工具类
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtilsUtils {

    public static String getNowDate() {
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();

        // 自定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化日期和时间
        String formattedDateTime = now.format(formatter);

        return formattedDateTime;
    }
}
