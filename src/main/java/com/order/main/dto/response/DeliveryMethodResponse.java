package com.order.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryMethodResponse {

    // 配送方式
    private String shippingId;

    // 配送方式名称
    private String shippingName;

    // 是否默认配送方式
    private boolean isDefault;

    // 此配送方式支持的快递公司
    private List<company> companies;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class company {

        // 快递公司名称
        private String shippingComName;

        // 快递公司代号
        private String shippingCom;

        // 是否默认
        private boolean isDefault;

    }

}
