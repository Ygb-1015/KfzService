package com.order.main.dto.requst;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryRequest {

    // 店铺ID
    @NotNull(message = "shopId不能为空")
    private Long shopId;

    // 订单ID
    @NotNull(message = "orderId不能为空")
    private Long orderId;

    // 配送方式ID
    @NotNull(message = "shippingId不能为空")
    private String shippingId;

    // 配送公司
    private String shippingCom;

    // 运单号
    private String shipmentNum;

    // 自定义
    private String userDefined;

    // 多个运单号
    private String moreShipmentNum;


}
