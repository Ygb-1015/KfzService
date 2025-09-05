package com.order.main.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

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

    /**
     * 将时间戳或日期字符串转换为指定格式的日期字符串
     * 如果是时间戳（数字），则转换为日期格式
     * 如果是日期字符串，则保留年月日部分返回
     *
     * @param input   时间戳（秒级或毫秒级）或日期字符串
     * @param pattern 期望的输出日期格式，默认为 "yyyy-MM-dd"
     * @return 格式化后的日期字符串，如果输入无效则返回空字符串
     */
    public static String formatToDateString(Object input, String pattern) {
        if (input == null) {
            return "";
        }

        // 定义默认格式
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd";
        }

        String inputStr = input.toString().trim();
        if (inputStr.isEmpty()) {
            return "";
        }

        try {
            // 尝试解析为数字（时间戳）
            if (inputStr.matches("\\d+")) {
                long timestamp = Long.parseLong(inputStr);

                // 判断是秒级还是毫秒级时间戳
                if (inputStr.length() == 10) { // 秒级时间戳（10位）
                    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
                    return dateTime.format(DateTimeFormatter.ofPattern(pattern));
                } else if (inputStr.length() == 13) { // 毫秒级时间戳（13位）
                    Instant instant = Instant.ofEpochMilli(timestamp);
                    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                    return dateTime.format(DateTimeFormatter.ofPattern(pattern));
                } else {
                    // 其他长度的数字，尝试按毫秒处理
                    Instant instant = Instant.ofEpochMilli(timestamp);
                    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                    return dateTime.format(DateTimeFormatter.ofPattern(pattern));
                }
            } else {
                // 输入是日期字符串，尝试解析并重新格式化
                return parseAndFormatDateString(inputStr, pattern);
            }
        } catch (Exception e) {
            // 如果解析失败，返回原始字符串或尝试其他方式处理
            return tryFallbackFormat(inputStr, pattern);
        }
    }

    /**
     * 解析并重新格式化日期字符串
     */
    private static String parseAndFormatDateString(String dateStr, String targetPattern) {
        // 常见的日期格式模式
        String[] possiblePatterns = {
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd",
                "yyyy/MM/dd HH:mm:ss",
                "yyyy/MM/dd",
                "yyyy年MM月dd日",
                "yyyy.MM.dd",
                "MM/dd/yyyy",
                "dd/MM/yyyy"
        };

        for (String pattern : possiblePatterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                TemporalAccessor temporal = formatter.parseBest(dateStr, LocalDateTime::from, LocalDate::from);

                if (temporal instanceof LocalDateTime) {
                    return ((LocalDateTime) temporal).format(DateTimeFormatter.ofPattern(targetPattern));
                } else if (temporal instanceof LocalDate) {
                    return ((LocalDate) temporal).format(DateTimeFormatter.ofPattern(targetPattern));
                }
            } catch (Exception e) {
                // 尝试下一种格式
                continue;
            }
        }

        // 如果所有格式都失败，返回原始字符串的年月日部分（如果可能）
        return extractDatePart(dateStr, targetPattern);
    }

    /**
     * 尝试从字符串中提取日期部分
     */
    private static String extractDatePart(String dateStr, String targetPattern) {
        // 简单的正则匹配来提取日期部分
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d{4}[-/年]\\d{1,2}[-/月]\\d{1,2})");
        java.util.regex.Matcher matcher = pattern.matcher(dateStr);

        if (matcher.find()) {
            String foundDate = matcher.group(1);
            // 尝试格式化找到的日期
            try {
                // 替换中文为横杠以便解析
                String normalized = foundDate.replace("年", "-").replace("月", "-").replace("日", "").replace("/", "-");
                LocalDate date = LocalDate.parse(normalized, DateTimeFormatter.ofPattern("yyyy-M-d"));
                return date.format(DateTimeFormatter.ofPattern(targetPattern));
            } catch (Exception e) {
                return foundDate; // 返回找到的原始日期部分
            }
        }

        return dateStr; // 如果无法提取，返回原始字符串
    }

    /**
     * 备用格式化方法
     */
    private static String tryFallbackFormat(String input, String pattern) {
        // 最后尝试：如果是纯数字但之前解析失败，可能是非常大的数字
        if (input.matches("\\d+")) {
            try {
                // 尝试作为毫秒时间戳处理
                long timestamp = Long.parseLong(input);
                Instant instant = Instant.ofEpochMilli(timestamp);
                LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
                return dateTime.format(DateTimeFormatter.ofPattern(pattern));
            } catch (Exception e) {
                return input; // 返回原始输入
            }
        }
        return input; // 返回原始输入
    }
}
