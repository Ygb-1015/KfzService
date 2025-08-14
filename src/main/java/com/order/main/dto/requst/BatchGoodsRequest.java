package com.order.main.dto.requst;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分批处理请求结构体
 * 用于B程序向A程序分批传输数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchGoodsRequest {
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 主任务id
     */
    private Long taskId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 当前批次号
     */
    private Integer batchNo;
    
    /**
     * 总批次数
     */
    private Integer totalBatches;
    
    /**
     * 当前批次数据
     */
    private List<ZhishuShopGoodsRequest> batchData;
    
    /**
     * 同步时间
     */
    private String currentDateTime;
    
    /**
     * 是否是最后一批
     */
    private Boolean isLastBatch;
}