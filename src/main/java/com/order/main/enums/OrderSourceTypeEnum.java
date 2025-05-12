package com.order.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单来源类型枚举
 */
@Getter
@AllArgsConstructor
public enum OrderSourceTypeEnum {

    PDD(1, "拼多多"),

    KFZ(2, "孔夫子");

    /**
     * code
     */
    private final Integer code;

    /**
     * message
     */
    private final String msg;

    public static List<Integer> getOrderSourceTypeEnumList() {
        List<Integer> orderSourceTypeEnumList = new ArrayList<>();
        for (OrderSourceTypeEnum item : values()) {
            orderSourceTypeEnumList.add(item.getCode());
        }
        return orderSourceTypeEnumList;
    }

    public static OrderSourceTypeEnum valueOfCode(Integer code) {
        for (OrderSourceTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new RuntimeException("未知操作库存类型code: " + code);
    }

}
