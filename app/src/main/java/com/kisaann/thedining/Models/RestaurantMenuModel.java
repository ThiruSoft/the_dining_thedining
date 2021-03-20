package com.kisaann.thedining.Models;

public class RestaurantMenuModel {
    private String name;
    private String image;
    private String city;
    private String area;
    private String address;
    private String about;
    private String restId;
    private String gstNo;
    private String timings;
    private String status;
    private String offerType;
    private String restaurantNo;
    private String restaurantOwnerNo;

    public RestaurantMenuModel() {
    }

    public RestaurantMenuModel(String name, String image, String city, String area, String address, String about, String restId, String gstNo, String timings, String status, String offerType, String restaurantNo, String restaurantOwnerNo) {
        this.name = name;
        this.image = image;
        this.city = city;
        this.area = area;
        this.address = address;
        this.about = about;
        this.restId = restId;
        this.gstNo = gstNo;
        this.timings = timings;
        this.status = status;
        this.offerType = offerType;
        this.restaurantNo = restaurantNo;
        this.restaurantOwnerNo = restaurantOwnerNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getRestaurantNo() {
        return restaurantNo;
    }

    public void setRestaurantNo(String restaurantNo) {
        this.restaurantNo = restaurantNo;
    }

    public String getRestaurantOwnerNo() {
        return restaurantOwnerNo;
    }

    public void setRestaurantOwnerNo(String restaurantOwnerNo) {
        this.restaurantOwnerNo = restaurantOwnerNo;
    }
}
