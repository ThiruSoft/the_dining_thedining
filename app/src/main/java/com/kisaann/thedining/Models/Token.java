package com.kisaann.thedining.Models;

public class Token {
    private String token;
    private String isServiceToken;
    private String Phone;

    public Token() {
    }

    public Token(String token, String isServiceToken, String phone) {
        this.token = token;
        this.isServiceToken = isServiceToken;
        Phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIsServiceToken() {
        return isServiceToken;
    }

    public void setIsServiceToken(String isServiceToken) {
        this.isServiceToken = isServiceToken;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
