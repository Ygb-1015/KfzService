package com.order.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    WAIT_FOR_SHIPMENT(1, "待发货"),

    SHIPMENT_WAIT_FOR_SIGN(2, "已发货待签收"),

    SIGNED(3, "已签收");

    /**
     * code
     */
    private final Integer code;

    /**
     * message
     */
    private final String msg;

    public static List<Integer> getOrderStatusEnumList() {
        List<Integer> orderStatusEnumList = new ArrayList<>();
        for (OrderStatusEnum item : values()) {
            orderStatusEnumList.add(item.getCode());
        }
        return orderStatusEnumList;
    }

}
