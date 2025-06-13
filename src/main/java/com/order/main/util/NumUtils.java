package com.order.main.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class NumUtils {

    /**
     * 对输入的 value 随机加减 [0.01, 0.1] 范围内的值
     *
     * @param value 输入值
     * @return 调整后的 BigDecimal 值，保留两位小数
     */
    public static BigDecimal randomAdjust(BigDecimal value) {
        // 创建 Random 实例
        Random random = new Random();

        // 生成 [0.01, 0.1] 范围内的随机值
        double deltaDouble = 0.01 + (0.09 * random.nextDouble());
        BigDecimal delta = new BigDecimal(Double.toString(deltaDouble));

        // 随机决定是加还是减
        if (random.nextBoolean()) {
            return value.add(delta).setScale(2, RoundingMode.HALF_UP);
        } else {
            return value.subtract(delta).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
