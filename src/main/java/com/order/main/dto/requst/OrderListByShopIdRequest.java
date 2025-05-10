package com.order.main.dto.requst;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListByShopIdRequest {

    private Long shopId;

    private List<String> orderSnList;

}
