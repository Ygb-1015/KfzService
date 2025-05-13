package com.order.main.util;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String convertUnicodeToChinese(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }


        Pattern pattern = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher matcher = pattern.matcher(input);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String unicodeHex = matcher.group(1);
            char ch = (char) Integer.parseInt(unicodeHex, 16); // 转换为 Unicode 字符
            matcher.appendReplacement(sb, String.valueOf(ch));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
