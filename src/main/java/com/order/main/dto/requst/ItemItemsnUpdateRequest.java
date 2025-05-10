package com.order.main.dto.requst;

import lombok.Data;

@Data
public class ItemItemsnUpdateRequest {

    // token
    private String token;

    // 商品编号
    private Long itemId;

    // 货号
    private String itemSn;

}
