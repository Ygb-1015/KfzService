package com.order.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    UNKNOWN("unkown", "未知"),

    SELLER("seller", "卖家"),

    BUYER("buyer", "买家");

    /**
     * code
     */
    private final String code;

    /**
     * message
     */
    private final String msg;

    public static List<String> getUserTypeEnumList() {
        List<String> userTypeEnumList = new ArrayList<>();
        for (UserTypeEnum item : values()) {
            userTypeEnumList.add(item.getCode());
        }
        return userTypeEnumList;
    }


}
