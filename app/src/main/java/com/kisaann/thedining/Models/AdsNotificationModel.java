package com.kisaann.thedining.Models;

public class AdsNotificationModel {
    private String category;
    private String adsTitle;
    private String adsTime;
    private String adsDate;
    private String adsPersonName;
    private String adsPersonNo;
    private String adsType;
    private String adsId;

    public AdsNotificationModel() {
    }

    public AdsNotificationModel(String category, String adsTitle, String adsTime, String adsDate, String adsPersonName, String adsPersonNo, String adsType, String adsId) {
        this.category = category;
        this.adsTitle = adsTitle;
        this.adsTime = adsTime;
        this.adsDate = adsDate;
        this.adsPersonName = adsPersonName;
        this.adsPersonNo = adsPersonNo;
        this.adsType = adsType;
        this.adsId = adsId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAdsTitle() {
        return adsTitle;
    }

    public void setAdsTitle(String adsTitle) {
        this.adsTitle = adsTitle;
    }

    public String getAdsTime() {
        return adsTime;
    }

    public void setAdsTime(String adsTime) {
        this.adsTime = adsTime;
    }

    public String getAdsDate() {
        return adsDate;
    }

    public void setAdsDate(String adsDate) {
        this.adsDate = adsDate;
    }

    public String getAdsPersonName() {
        return adsPersonName;
    }

    public void setAdsPersonName(String adsPersonName) {
        this.adsPersonName = adsPersonName;
    }

    public String getAdsPersonNo() {
        return adsPersonNo;
    }

    public void setAdsPersonNo(String adsPersonNo) {
        this.adsPersonNo = adsPersonNo;
    }

    public String getAdsType() {
        return adsType;
    }

    public void setAdsType(String adsType) {
        this.adsType = adsType;
    }

    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }
}
