package com.order.main.dto.requst;


public class UpdateArtNoRequest {

    // 店铺Id
    private Long shopId;

    // 商品编号
    private Long productId;

    // 货号
    private String artNo;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getArtNo() {
        return artNo;
    }

    public void setArtNo(String artNo) {
        this.artNo = artNo;
    }
}
