package com.order.main.dto.requst;

import lombok.Data;

@Data
public class DeliverKfzRequest {

    // 订单编号
    private Integer orderId;

    // 配送方式。取值参考kongfz.delivery.method.list接口的返回值。
    private String shippingId;

    // 快递公司。当shippingId!=noLogistics时，此参数为必填。
    private String shippingCom;

    // 快递单号。当shippingId!=noLogistics时，此参数为必填。
    private String shipmentNum;

    // 用户自定义物流公司。当shippingCom=other时，此参数为必填。
    private String userDefined;

    // 填写更多的快递单号，以逗号分隔。
    private String moreShipmentNum;

}
