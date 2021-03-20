package com.kisaann.thedining.Models;

public class Category {
    private String Name;
    private String restaurantMenuId;
    private String Image;
    private String timeFrom;
    private String timeTo;
    private String type;
    private String offerType;
    private String categoryType;
    public Category() {
    }

    public Category(String name, String restaurantMenuId, String image, String timeFrom, String timeTo, String type, String offerType, String categoryType) {
        Name = name;
        this.restaurantMenuId = restaurantMenuId;
        Image = image;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.type = type;
        this.offerType = offerType;
        this.categoryType = categoryType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRestaurantMenuId() {
        return restaurantMenuId;
    }

    public void setRestaurantMenuId(String restaurantMenuId) {
        this.restaurantMenuId = restaurantMenuId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
