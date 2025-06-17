package com.order.main.dto.requst;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingSoldOutRequest {

    /**
     * 平台类型：1 拼多多 2-孔夫子
     */
    private Integer platformType;

    /**
     * 日志类型：1-上架 2-下架
     */
    private Integer logType;

    /**
     * 操作类型：1-发布任务 2-订单
     */
    private Integer operationType;

    /**
     * 三方平台订单编号
     */
    private String orderSn;

    /**
     * 平台店铺Id
     */
    private Long mallId;

    /**
     * 商品信息列表
     */
    private List<GoodsItem> goodsItems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsItem {

        /**
         * 平台商品Id
         */
        private String platformId;

    }

}
