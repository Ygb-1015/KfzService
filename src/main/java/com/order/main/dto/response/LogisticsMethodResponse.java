package com.order.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物流方式
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsMethodResponse {

    // 物流方式Id
    private String methodId;

    // 物流方式名称
    private String methodName;

}
