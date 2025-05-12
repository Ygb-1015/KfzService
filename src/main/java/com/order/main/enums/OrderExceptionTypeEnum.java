package com.order.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单异常类型枚举
 */
@Getter
@AllArgsConstructor
public enum OrderExceptionTypeEnum {

    INVENTORY_EXCEPTION("inventoryException", "库存异常"),

    GOODS_SOURCE_UNKNOWN_EXCEPTION("goodsSourceUnknownException", "未知商品来源");

    /**
     * code
     */
    private final String code;

    /**
     * message
     */
    private final String msg;

    public static List<String> getOrderExceptionTypeEnumList() {
        List<String> orderExceptionTypeEnumList = new ArrayList<>();
        for (OrderExceptionTypeEnum item : values()) {
            orderExceptionTypeEnumList.add(item.getCode());
        }
        return orderExceptionTypeEnumList;
    }

    public static OrderExceptionTypeEnum valueOfCode(String code) {
        for (OrderExceptionTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new RuntimeException("未知操作库存类型code: " + code);
    }

}
