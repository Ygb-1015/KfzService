package com.order.main.dto.response;

import lombok.Data;

import java.util.Date;

/**
 * 店铺主表视图对象 t_shop
 *
 * @author yxy
 * @date 2025-03-10
 */
@Data
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

    /**
     * 创建人
     */
    private Long createBy;

}