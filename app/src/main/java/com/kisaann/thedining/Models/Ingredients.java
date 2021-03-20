package com.kisaann.thedining.Models;

public class Ingredients {
    private String keyId;
    private String name;
    private Long quantity;
    private String quantityType;

    public Ingredients() {
    }

    public Ingredients(String keyId, String name, Long quantity, String quantityType) {
        this.keyId = keyId;
        this.name = name;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
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
