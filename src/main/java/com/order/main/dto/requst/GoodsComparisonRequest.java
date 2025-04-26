package com.order.main.dto.requst;


import java.util.List;

public class GoodsComparisonRequest {

    private Long shopId;

    private Long userId;

    private List<ZhishuShopGoodsRequest> zhishuShopGoodsRequestList;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ZhishuShopGoodsRequest> getZhishuShopGoodsRequestList() {
        return zhishuShopGoodsRequestList;
    }

    public void setZhishuShopGoodsRequestList(List<ZhishuShopGoodsRequest> zhishuShopGoodsRequestList) {
        this.zhishuShopGoodsRequestList = zhishuShopGoodsRequestList;
    }
}
