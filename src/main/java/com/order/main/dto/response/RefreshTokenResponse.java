package com.order.main.dto.response;

public class RefreshTokenResponse {

    // 错误码
    private Integer errCode;

    // 错误信息
    private String errMessage;

    // 结果
    private Result result;

    // 状态
    private Boolean status;

    public static class Result{

        // access_token
        private String accessToken;

        // refresh_token
        private String refreshToken;

        // 用户ID
        private Long userId;

        // 过期时间
        private Long expiresAt;

        // refresh_token 过期时间
        private Long refreshExpiresAt;

        // 授权类型
        private String grantType;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(Long expiresAt) {
            this.expiresAt = expiresAt;
        }

        public Long getRefreshExpiresAt() {
            return refreshExpiresAt;
        }

        public void setRefreshExpiresAt(Long refreshExpiresAt) {
            this.refreshExpiresAt = refreshExpiresAt;
        }

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            this.grantType = grantType;
        }
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
