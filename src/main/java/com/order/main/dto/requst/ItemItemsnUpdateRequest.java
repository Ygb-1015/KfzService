package com.order.main.dto.requst;

public class ItemItemsnUpdateRequest {

    // token
    private String token;

    // 商品编号
    private Long itemId;

    // 货号
    private String itemSn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemSn() {
        return itemSn;
    }

    public void setItemSn(String itemSn) {
        this.itemSn = itemSn;
    }
}
