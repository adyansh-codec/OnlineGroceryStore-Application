package org.softuni.onlinegrocery.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category_new")
public class CategoryNew extends BaseEntity {

    private String name;
    private String description;
    private String imageUrl;
    private List<Subcategory> subcategories;

    public CategoryNew() {
    }

    public CategoryNew(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @OneToMany(mappedBy = "categoryNew", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
