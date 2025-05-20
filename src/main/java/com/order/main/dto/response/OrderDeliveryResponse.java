package com.order.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryResponse {

    private Long orderId;

    private String remark;

    private String updateTime;

}
