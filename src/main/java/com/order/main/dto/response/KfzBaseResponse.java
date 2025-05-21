package com.order.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KfzBaseResponse<T> {

    // 请求Id
    private String requestId;

    // 请求方法
    private String requestMethod;

    // 请求成功返回的数据
    private T successResponse;

    // 请求失败返回的错误信息
    public ErrorResponse errorResponse;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        // 请求失败返回的错误码
        private Long code;

        // 请求失败返回的错误信息
        private String msg;

        // 请求失败返回的子错误码
        private String subCode;

        // 请求失败返回的子错误信息
        private String subMsg;
    }

}
