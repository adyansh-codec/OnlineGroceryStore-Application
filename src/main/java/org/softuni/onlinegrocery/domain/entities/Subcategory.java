package org.softuni.onlinegrocery.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subcategory")
public class Subcategory extends BaseEntity {

    private String name;
    private String description;
    private String imageUrl;
    private CategoryNew categoryNew;
    private List<ProductNew> products;

    public Subcategory() {
    }

    public Subcategory(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Column(name = "name", nullable = false)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_new_id", nullable = false)
    public CategoryNew getCategoryNew() {
        return categoryNew;
    }

    public void setCategoryNew(CategoryNew categoryNew) {
        this.categoryNew = categoryNew;
    }

    @OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<ProductNew> getProducts() {
        return products;
    }

    public void setProducts(List<ProductNew> products) {
        this.products = products;
    }
}
