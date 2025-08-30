package org.softuni.onlinegrocery.domain.models.service;

import java.util.List;

public class CategoryNewServiceModel {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private List<SubcategoryServiceModel> subcategories;

    public CategoryNewServiceModel() {
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

    public List<SubcategoryServiceModel> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubcategoryServiceModel> subcategories) {
        this.subcategories = subcategories;
    }
}
