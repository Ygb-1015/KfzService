package com.order.main.dto.response;

import java.util.Date;

/**
 * 店铺主表视图对象 t_shop
 *
 * @author yxy
 * @date 2025-03-10
 */
public class ShopVo {

    /**
     * ID
     */
    private Long id;

    /**
     * 三方店铺id
     */
    private Long mallId;

    /**
     * 店铺类型  1 拼多多
     */
    private String shopType;

    /**
     * 分组
     */
    private String shopGroup;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺名称（对应平台的店铺名称）
     */
    private String shopAliasName;

    /**
     * 是否授权  0未授权 1已授权 2已过期
     */
    private String shopAuthorize;

    /**
     * 到期时间
     */
    private Date expirationTime;

    /**
     * 添加时间
     */
    private Date addTime;

    /**
     * 店铺key
     */
    private String shopKey;

    /**
     * token
     */
    private String token;

    /**
     * refreshToken
     */
    private String refreshToken;

    /**
     * 店铺状态（0正常 1停用）
     */
    private String status;

    /**
     * 租户编码
     */
    private String tenant_id;

    /**
     * 上次订单同步时间
     */
    private Long startUpdatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMallId() {
        return mallId;
    }

    public void setMallId(Long mallId) {
        this.mallId = mallId;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopGroup() {
        return shopGroup;
    }

    public void setShopGroup(String shopGroup) {
        this.shopGroup = shopGroup;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAliasName() {
        return shopAliasName;
    }

    public void setShopAliasName(String shopAliasName) {
        this.shopAliasName = shopAliasName;
    }

    public String getShopAuthorize() {
        return shopAuthorize;
    }

    public void setShopAuthorize(String shopAuthorize) {
        this.shopAuthorize = shopAuthorize;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getShopKey() {
        return shopKey;
    }

    public void setShopKey(String shopKey) {
        this.shopKey = shopKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    public Long getStartUpdatedAt() {
        return startUpdatedAt;
    }

    public void setStartUpdatedAt(Long startUpdatedAt) {
        this.startUpdatedAt = startUpdatedAt;
    }
}