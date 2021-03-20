package com.kisaann.thedining.Models;

public class QRCodeModel {
    private String qrImage;
    private String qrName;
    private String restaurantName;
    private String restaurantId;
    private String restaurantOwnerName;
    private String restaurantOwnerNo;
    private String date;
    private String time;
    private String dateTime;
    private String restaurantKitchenNo;

    public QRCodeModel() {
    }

    public QRCodeModel(String qrImage, String qrName, String restaurantName, String restaurantId, String restaurantOwnerName, String restaurantOwnerNo, String date, String time, String dateTime, String restaurantKitchenNo) {
        this.qrImage = qrImage;
        this.qrName = qrName;
        this.restaurantName = restaurantName;
        this.restaurantId = restaurantId;
        this.restaurantOwnerName = restaurantOwnerName;
        this.restaurantOwnerNo = restaurantOwnerNo;
        this.date = date;
        this.time = time;
        this.dateTime = dateTime;
        this.restaurantKitchenNo = restaurantKitchenNo;
    }

    public String getQrImage() {
        return qrImage;
    }

    public void setQrImage(String qrImage) {
        this.qrImage = qrImage;
    }

    public String getQrName() {
        return qrName;
    }

    public void setQrName(String qrName) {
        this.qrName = qrName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantOwnerName() {
        return restaurantOwnerName;
    }

    public void setRestaurantOwnerName(String restaurantOwnerName) {
        this.restaurantOwnerName = restaurantOwnerName;
    }

    public String getRestaurantOwnerNo() {
        return restaurantOwnerNo;
    }

    public void setRestaurantOwnerNo(String restaurantOwnerNo) {
        this.restaurantOwnerNo = restaurantOwnerNo;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRestaurantKitchenNo() {
        return restaurantKitchenNo;
    }

    public void setRestaurantKitchenNo(String restaurantKitchenNo) {
        this.restaurantKitchenNo = restaurantKitchenNo;
    }
}
