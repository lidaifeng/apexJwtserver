package com.apex.jwtserver.authorization.model;

/**
 * @Description: Token的Model类， 可以增加字段提高安全性，例如时间戳、url签名
 * @Date: Created in 2020/07/18
 */
public class TokenModel {

    private int userId;

    // 随机生成的uuid
    private String token;

    public TokenModel(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenModel{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }
}
