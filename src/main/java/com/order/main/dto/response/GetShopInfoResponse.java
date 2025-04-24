package com.order.main.dto.response;

/**
 * 获取孔夫子店铺信息响应体
 */
public class GetShopInfoResponse {

    // 店铺状态。new:新申请,onSale:开通,pause:暂停,close:关闭
    private String shopStatus;

    // 用户头像
    private String userPic;

    // 用户昵称
    private String nickname;

    // 店铺名称
    private String shopName;

    // 店铺编号
    private Long shopId;

    // 店铺类型。shop:书店,bookstall:书摊
    private String shopType;

    // 用户编号
    private Long userId;

    public String getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(String shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
