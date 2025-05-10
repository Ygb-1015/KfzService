package com.order.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum ConfirmStatusEnum {

    NOT_SOLD(0, "未成交"),

    SOLD(2, "已成交"),

    CANCEL(3, "已取消");

    /**
     * code
     */
    private final Integer code;

    /**
     * message
     */
    private final String msg;

    public static List<Integer> getConfirmStatusEnumList() {
        List<Integer> confirmStatusEnumList = new ArrayList<>();
        for (ConfirmStatusEnum item : values()) {
            confirmStatusEnumList.add(item.getCode());
        }
        return confirmStatusEnumList;
    }

}
