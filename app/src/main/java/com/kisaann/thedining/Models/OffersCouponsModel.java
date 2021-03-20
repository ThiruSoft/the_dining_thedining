package com.kisaann.thedining.Models;

public class OffersCouponsModel {
    private String offerTitle;
    private String offerDescription;
    private String date;
    private String time;
    private String dateTime;
    private String couponCode;
    private String status;
    private String phoneNo;
    private String type;
    private String discount;
    private String image;
    private String visibility;
    private String offerType;
    public OffersCouponsModel() {
    }

    public OffersCouponsModel(String offerTitle, String offerDescription, String date, String time, String dateTime, String couponCode, String status, String phoneNo, String type, String discount, String image, String visibility, String offerType) {
        this.offerTitle = offerTitle;
        this.offerDescription = offerDescription;
        this.date = date;
        this.time = time;
        this.dateTime = dateTime;
        this.couponCode = couponCode;
        this.status = status;
        this.phoneNo = phoneNo;
        this.type = type;
        this.discount = discount;
        this.image = image;
        this.visibility = visibility;
        this.offerType = offerType;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }
}
