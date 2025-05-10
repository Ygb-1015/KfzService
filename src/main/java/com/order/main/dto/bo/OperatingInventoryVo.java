package com.order.main.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 库存操作Vo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingInventoryVo {

    /**
     * 操作类型 1:增加库存 2:减少库存
     */
    private Integer operationType;

    /**
     * 店铺类型 1:拼多多 2:孔夫子
     */
    private Integer shopType;

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

        /**
         * 商品数量
         */
        private Integer count;

    }

}
