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

    SHIPPED_REFUNDING("Shipped-Refunding", "退货退款中"),

    SELLER_REVIEWED("sellerReviewed", "待评价"),

    SUCCESSFUL("Successful", "成功完成"),

    REFUND_DEALD("RefundDeald", "已退货退款"),

    SHIPPED_REFUNDED("ShippedRefunded", "退款完成"),

    BUYER_CANCELLED("BuyerCancelled", "买家取消"),

    SELLER_CANCELLED_BEFORE_CONFIRM("SellerCancelledBeforeConfirm", "卖家取消"),

    ADMIN_CLOSED_BEFORE_CONFIRM("AdminClosedBeforeConfirm", "管理员关闭");

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
