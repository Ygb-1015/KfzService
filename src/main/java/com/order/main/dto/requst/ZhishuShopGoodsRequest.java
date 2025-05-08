package com.order.main.dto.requst;

import lombok.Data;

/**
 * 商品信息业务对象 zhishu_shop_goods
 *
 * @author Lion Li
 * @date 2025-03-07
 */
@Data
public class ZhishuShopGoodsRequest {

    /**
     * 商品id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 产品编码
     */
    private String productId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * isbn
     */
    private String isbn;

    /**
     * 货号
     */
    private String artNo;

    /**
     * 期初库存
     */
    private Long stock;

    /**
     * 标准售价
     */
    private Long price;

    /**
     * 品相
     */
    private Integer conditionCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 商品编号
     */
    private String itemNumber;

    /**
     * 商品定价
     */
    private Long fixPrice;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 书图片
     */
    private String bookPic;

    /**
     * 是否已进行货号转换：0-未转换 1-已转换
     */
    private Integer isArtNoConversion;

}
