package com.order.main.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



/**
 * 记录发布数据视图对象 t_shop_goods_published
 *
 * @author yxy
 * @date 2025-04-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopGoodsPublishedVo implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 图书主键
     */
    private String shopGoodsId;

    private String goodsName;

    /**
     * 发布的店铺ids
     */
    private String shopId;

    private String shopName;

    /**
     * 平台商品id
     */
    private String platformId;

    /**
     * 状态（0正常 1停用）
     */
    private String status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopGoodsId() {
        return shopGoodsId;
    }

    public void setShopGoodsId(String shopGoodsId) {
        this.shopGoodsId = shopGoodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
