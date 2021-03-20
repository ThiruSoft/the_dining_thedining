package com.kisaann.thedining.Models;

public class RestBannersModel {
    private String restName;
    private String restId;
    private String bannerDes;
    private String date;
    private String time;
    private String dateTime;
    private String imageURL;

    public RestBannersModel() {
    }

    public RestBannersModel(String restName, String restId, String bannerDes, String date, String time, String dateTime, String imageURL) {
        this.restName = restName;
        this.restId = restId;
        this.bannerDes = bannerDes;
        this.date = date;
        this.time = time;
        this.dateTime = dateTime;
        this.imageURL = imageURL;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getBannerDes() {
        return bannerDes;
    }

    public void setBannerDes(String bannerDes) {
        this.bannerDes = bannerDes;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
