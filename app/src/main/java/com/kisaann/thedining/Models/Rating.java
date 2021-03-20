package com.kisaann.thedining.Models;

public class Rating {
    private String userPhone;
    private String userName;
    private String restId;
    private String rateValue;
    private String comment;
    private String date;
    private String time;
    private String orderId;

    public Rating() {
    }

    public Rating(String userPhone, String userName, String restId, String rateValue, String comment, String date, String time, String orderId) {
        this.userPhone = userPhone;
        this.userName = userName;
        this.restId = restId;
        this.rateValue = rateValue;
        this.comment = comment;
        this.date = date;
        this.time = time;
        this.orderId = orderId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
