package com.kisaann.thedining.Models;

public class InventoryModel {
    private String restaurantMenuId;
    private String key_ID;
    private String name;
    private Long quantity;
    private String quantityType;

    public InventoryModel() {
    }

    public InventoryModel(String restaurantMenuId, String key_ID, String name, Long quantity, String quantityType) {
        this.restaurantMenuId = restaurantMenuId;
        this.key_ID = key_ID;
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public String getRestaurantMenuId() {
        return restaurantMenuId;
    }

    public void setRestaurantMenuId(String restaurantMenuId) {
        this.restaurantMenuId = restaurantMenuId;
    }

    public String getKey_ID() {
        return key_ID;
    }

    public void setKey_ID(String key_ID) {
        this.key_ID = key_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }
}
