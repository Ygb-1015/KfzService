package com.order.main.dto.response;

public class KfzBaseResponse<T> {

    // 请求Id
    private String requestId;

    // 请求方法
    private String requestMethod;

    // 请求成功返回的数据
    private T successResponse;

    // 请求失败返回的错误信息
    public ErrorResponse errorResponse;

    // Getter 和 Setter 方法

    public static class ErrorResponse {
        // 请求失败返回的错误码
        private Long code;

        // 请求失败返回的错误信息
        private String msg;

        // 请求失败返回的子错误码
        private Long subCode;

        // 请求失败返回的子错误信息
        private String subMsg;

        // data (假设不需要此字段)
        // private String data;

        // Getter 和 Setter 方法
        public Long getCode() { return code; }
        public void setCode(Long code) { this.code = code; }

        public String getMsg() { return msg; }
        public void setMsg(String msg) { this.msg = msg; }

        public Long getSubCode() { return subCode; }
        public void setSubCode(Long subCode) { this.subCode = subCode; }

        public String getSubMsg() { return subMsg; }
        public void setSubMsg(String subMsg) { this.subMsg = subMsg; }

        // 如果需要data字段，请添加其getter和setter方法
    }

    // Getter 和 Setter 方法
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getRequestMethod() { return requestMethod; }
    public void setRequestMethod(String requestMethod) { this.requestMethod = requestMethod; }

    public T getSuccessResponse() { return successResponse; }
    public void setSuccessResponse(T successResponse) { this.successResponse = successResponse; }

    public ErrorResponse getErrorResponse() { return errorResponse; }
    public void setErrorResponse(ErrorResponse errorResponse) { this.errorResponse = errorResponse; }
}
