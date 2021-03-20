package com.kisaann.thedining.Models;

import java.util.List;

public class Food {
    private String name ;
    private String image;
    private String description;
    private String price;
    private String discount;
    private String menuId;
    private String restaurantId;
    private String information;
    private String timeFrom;
    private String timeTo;
    private String quantity;
    private String availability;
    private String productId;
    private String type;
    private String categoryName;
    private String categoryType;
    private String department;
    private String particular;
    private String gst;
    private String happyHour;
    private String kitchen;
    private String captainName;
    private Long FoodQuantity;
    private List<Ingredients> ingredients; // list of ingredients
    public Food() {
    }

    public Food(String name, String image, String description, String price, String discount, String menuId, String restaurantId, String information, String timeFrom, String timeTo, String quantity, String availability, String productId, String type, String categoryName, String categoryType, String department, String particular, String gst, String happyHour, String kitchen, String captainName, Long foodQuantity, List<Ingredients> ingredients) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.menuId = menuId;
        this.restaurantId = restaurantId;
        this.information = information;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.quantity = quantity;
        this.availability = availability;
        this.productId = productId;
        this.type = type;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.department = department;
        this.particular = particular;
        this.gst = gst;
        this.happyHour = happyHour;
        this.kitchen = kitchen;
        this.captainName = captainName;
        FoodQuantity = foodQuantity;
        this.ingredients = ingredients;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getHappyHour() {
        return happyHour;
    }

    public void setHappyHour(String happyHour) {
        this.happyHour = happyHour;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public Long getFoodQuantity() {
        return FoodQuantity;
    }

    public void setFoodQuantity(Long foodQuantity) {
        FoodQuantity = foodQuantity;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }
}
