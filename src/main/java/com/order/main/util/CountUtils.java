package com.order.main.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CountUtils {


    /**
     * 计算百分比
     * @param total 总数
     * @param num   数量
     */
    public static String calculatePercentage(int total,int num){
        BigDecimal totalBigDecimal = new BigDecimal(total);
        BigDecimal numBigDecimal = new BigDecimal(num);
        BigDecimal percentage = numBigDecimal
            .divide(totalBigDecimal, 4, RoundingMode.HALF_UP) // 保留4位小数
            .multiply(new BigDecimal(100)) // 转换为百分比
            .setScale(2, RoundingMode.HALF_UP); // 保留2位小数
        return String.valueOf(percentage);
    }
}
