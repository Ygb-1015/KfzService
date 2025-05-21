package com.order.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum AfterSalesStatusEnum {

    NONE(0, "无售后"),

    BUYER_WAIT_FOR_REFUND(2, "买家申请退款，待商家处理"),

    REFUND_WAIT_FOR_SELLER(3, "退货退款，待商家处理"),

    SELLER_AGREES_REFUND_IN_PROGRESS(4, "商家同意退款，退款中"),

    PLATFORM_AGREE_REFUND_IN_PROGRESS(5, "平台同意退款，退款中"),

    REJECT_REFUND_WAIT_FOR_BUYER(6, "驳回退款，待买家处理"),

    AGREE_REFUND_WAIT_FOR_BUYER(7, "已同意退货退款,待用户发货"),

    PLATFORM_IN_PROGRESS(8, "平台处理中"),

    PLATFORM_REJECT_REFUND(9, "平台拒绝退款，退款关闭"),

    REFUND_SUCCESS(10, "退款成功"),

    BUYER_REVOKE(11, "买家撤销"),

    BUYER_OVERDUE_REFUND_FAILED(12, "买家逾期未处理，退款失败"),

    BUYER_OVERDUE_OVER_VALIDITY_PERIOD(13, "买家逾期，超过有效期"),

    EXCHANGE_AND_REPLACEMENT_WAIT_FOR_SELLER(14, "换货补寄待商家处理"),

    EXCHANGE_AND_REPLACEMENT_WAIT_FOR_BUYER(15, "换货补寄待用户处理"),

    EXCHANGE_AND_REPLACEMENT_SUCCESS(16, "换货补寄成功"),

    EXCHANGE_AND_REPLACEMENT_FAILED(17, "换货补寄失败"),

    EXCHANGE_AND_REPLACEMENT_WAIT_FOR_BUYER_CONFIRM(18, "换货补寄待用户确认完成"),

    WAIT_FOR_SELLER_AGREE_REPAIR(21, "待商家同意维修"),

    WAIT_FOR_BUYER_SHIPMENT_CONFIRM(22, "待用户确认发货"),

    REPAIR_CLOSE(24, "维修关闭"),

    REPAIR_SUCCESS(25, "维修成功"),

    WAIT_FOR_BUYER_RECEIVE_CONFIRM(27, "待用户确认收货"),

    AGREED_REFUSE_REFUND_WAIT_FOR_BUYER(31, "已同意拒收退款，待用户拒收"),

    REPLACEMENT_WAIT_FOR_SELLER_SHIPMENT(32, "补寄待商家发货"),

    AGREE_REFUND_AFTER_RECALL_WAIT_FOR_SELLER(33, "同意召回后退款，待商家召回"),

    SHIPPED_RETURNING(600, "申请退货中"),

    SHIPPED_RETURN_REJECTED(601, "拒绝退货中"),

    RETURN_PENDING(602, "待买家退货"),

    RETURNED_TO_RECEIPT(603, "待卖家确认收货并退款"),

    ;

    /**
     * code
     */
    private final Integer code;

    /**
     * message
     */
    private final String msg;

    public static List<Integer> getAfterSalesStatusEnumList() {
        List<Integer> afterSalesStatusEnumList = new ArrayList<>();
        for (AfterSalesStatusEnum item : values()) {
            afterSalesStatusEnumList.add(item.getCode());
        }
        return afterSalesStatusEnumList;
    }

}
