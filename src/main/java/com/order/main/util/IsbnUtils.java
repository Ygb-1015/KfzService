package com.order.main.util;

import java.util.Random;

public class IsbnUtils {
    public static String generateRandomIsbn() {
        // 生成符合ISBN-13格式的随机号码
        // 978是图书的EAN前缀
        StringBuilder isbn = new StringBuilder("678");

        // 添加9位随机数字
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            isbn.append(random.nextInt(10));
        }

        // 计算校验位
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += digit * (i % 2 == 0 ? 1 : 3);
        }
        int checkDigit = (10 - (sum % 10)) % 10;

        // 添加校验位
        isbn.append(checkDigit);

        return isbn.toString();
    }
}
