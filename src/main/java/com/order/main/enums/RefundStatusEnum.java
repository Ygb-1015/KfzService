package com.order.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum RefundStatusEnum {

    NONE_OR_CLOSE(1, "无售后或售后关闭"),

    AFTER_SALES_PROCESSING(2, "售后处理中"),

    REFUND_IN_PROGRESSING(3, "退款中"),

    REFUND_SUCCESS(4, "退款成功");

    /**
     * code
     */
    private final Integer code;

    /**
     * message
     */
    private final String msg;

    public static List<Integer> getRefundStatusEnumList() {
        List<Integer> refundStatusEnumList = new ArrayList<>();
        for (RefundStatusEnum item : values()) {
            refundStatusEnumList.add(item.getCode());
        }
        return refundStatusEnumList;
    }

}
