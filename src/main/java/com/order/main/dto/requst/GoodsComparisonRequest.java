package com.order.main.dto.requst;


import lombok.Data;

import java.util.List;

@Data
public class GoodsComparisonRequest {

    private Long shopId;

    private Long userId;

    private String currentDateTime;

    private List<ZhishuShopGoodsRequest> zhishuShopGoodsRequestList;

}
