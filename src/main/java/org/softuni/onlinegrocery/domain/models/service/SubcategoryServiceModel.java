package org.softuni.onlinegrocery.domain.models.service;

import java.util.List;

public class SubcategoryServiceModel {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String categoryNewId;
    private String categoryNewName;
    private List<ProductNewServiceModel> products;

    public SubcategoryServiceModel() {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public List<ProductNewServiceModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductNewServiceModel> products) {
        this.products = products;
    }
}
