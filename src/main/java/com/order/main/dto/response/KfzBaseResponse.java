package com.order.main.dto.response;

public class KfzBaseResponse<T> {

    // 请求Id
    private String requestId;

    // 请求方法
    private String requestMethod;

    // 请求成功返回的数据
    private T successResponse;

    // 请求失败返回的错误信息
    private ErrorResponse errorResponse;

    public static class ErrorResponse {

        // 请求失败返回的错误码
        private String code;

        // 请求失败返回的错误信息
        private String msg;

        // 请求失败返回的子错误码
        private String subCode;

        // 请求失败返回的子错误信息
        private String subMsg;

        // data
        private String data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSubCode() {
            return subCode;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }

        public String getSubMsg() {
            return subMsg;
        }

        public void setSubMsg(String subMsg) {
            this.subMsg = subMsg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public T getSuccessResponse() {
        return successResponse;
    }

    public void setSuccessResponse(T successResponse) {
        this.successResponse = successResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
