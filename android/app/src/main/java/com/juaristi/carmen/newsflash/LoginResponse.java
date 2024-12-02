package com.juaristi.carmen.newsflash;
public class LoginResponse {
    private String sessionToken;

    public String getToken() {
        return sessionToken;
    }

    public void setToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}