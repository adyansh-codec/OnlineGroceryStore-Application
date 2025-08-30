package org.softuni.onlinegrocery.domain.models.service;

import java.math.BigDecimal;

public class ProductNewServiceModel {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Integer quantity;
    private String brand;
    private String unit;
    private BigDecimal pua; // Packaged Unit A - Weight/Volume
    private BigDecimal pub; // Packaged Unit B - Weight/Volume  
    private BigDecimal puc; // Packaged Unit C - Weight/Volume
    private String itemUnit; // JSON structure for unit details
    private Boolean isActive;
    private String subcategoryId;
    private String subcategoryName;
    private String categoryNewId;
    private String categoryNewName;

    public ProductNewServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPua() {
        return pua;
    }

    public void setPua(BigDecimal pua) {
        this.pua = pua;
    }

    public BigDecimal getPub() {
        return pub;
    }

    public void setPub(BigDecimal pub) {
        this.pub = pub;
    }

    public BigDecimal getPuc() {
        return puc;
    }

    public void setPuc(BigDecimal puc) {
        this.puc = puc;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getCategoryNewId() {
        return categoryNewId;
    }

    public void setCategoryNewId(String categoryNewId) {
        this.categoryNewId = categoryNewId;
    }

    public String getCategoryNewName() {
        return categoryNewName;
    }

    public void setCategoryNewName(String categoryNewName) {
        this.categoryNewName = categoryNewName;
    }
}