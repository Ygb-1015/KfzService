package com.order.main.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopGoodsPublishedLog {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品id
     */
    private Long shopGoodsId;

    /**
     * 已发布商品id
     */
    private Long shopGoodsPublishedId;

    /**
     * 平台商品id
     */
    private String platformId;

    /**
     * 相关Id
     */
    private String aboutId;

    /**
     * 平台类型 1 拼多多 2-孔夫子
     */
    private Integer platformType;

    /**
     * 日志类型 1-上架 2-下架
     */
    private Integer logType;

    /**
     * 操作类型 1-发布任务 2-订单
     */
    private Integer operationType;

    /**
     * 状态 0-成功 1-失败
     */
    private Integer status;

    /**
     * 租户编码
     */
    private String tenantId;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

}
