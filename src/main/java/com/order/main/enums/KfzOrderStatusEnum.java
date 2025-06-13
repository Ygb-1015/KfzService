package com.order.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum KfzOrderStatusEnum {

    TRADING("Trading", "交易中"),

    PENDING("Pending", "待确认"),

    CONFIRMED_TO_PAY("ConfirmedToPay", "待付款"),

    PAID_TO_SHIP("PaidToShip", "待发货"),

    SHIPPED_TO_RECEIPT("ShippedToReceipt", "待确认收货"),

    PAID_REFUNDING("Paid-Refunding", "发货前 - 申请退款中"),

    PAID_REFUND_REJECTED("Paid-RefundRejected", "发货前 - 拒绝退款中"),

    PAID_REFUNDED("PaidRefunded", "发货前 - 已退款"),

    SHIPPED_REFUNDING("Shipped-Refunding", "发货后 - 申请退款中"),

    SHIPPED_REFUND_REJECTED("Shipped-RefundRejected", "发货后 - 拒绝退款中"),

    SHIPPED_REFUNDED("ShippedRefunded", "发货后 - 已退款"),

    SHIPPED_RETURNING("Shipped-Returning", "申请退货中"),

    SHIPPED_RETURN_REJECTED("Shipped-ReturnRejected", "拒绝退货中"),

    RETURN_PENDING("ReturnPending", "待买家退货"),

    RETURNED_TO_RECEIPT("ReturnedToReceipt", "待卖家确认收货并退款"),

    SHIPPED_RETURNED("ShippedReturned", "已退货退款"),

    SUCCESSFUL("Successful", "成功完成"),

    BUYER_CANCELLED_BEFORE_CONFIRM("BuyerCancelledBeforeConfirm", "卖家确认前买家取消"),

    BUYER_CANCELLED_BEFORE_PAY("BuyerCancelledBeforePay", "付款前买家取消"),

    SELLER_CANCELLED_BEFORE_CONFIRM("SellerCancelledBeforeConfirm", "卖家确认前卖家取消"),

    SELLER_CLOSED_BEFORE_PAY("SellerClosedBeforePay", "付款前卖家关闭"),

    ADMIN_CLOSED_BEFORE_CONFIRM("AdminClosedBeforeConfirm", "管理员关闭"),

    SELLER_CANCELLED_AFTER_PAY("SellerCancelledAfterPay", "付款后发货前卖家取消订单");

    /**
     * code
     */
    private final String code;

    /**
     * message
     */
    private final String msg;

    public static List<String> getOrderStatusEnumList() {
        List<String> orderStatusEnumList = new ArrayList<>();
        for (KfzOrderStatusEnum item : values()) {
            orderStatusEnumList.add(item.getCode());
        }
        return orderStatusEnumList;
    }


}
