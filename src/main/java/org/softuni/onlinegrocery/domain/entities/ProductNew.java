package org.softuni.onlinegrocery.domain.entities;

import org.hibernate.annotations.Type;
import org.softuni.onlinegrocery.domain.converters.JsonConverter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_new")
public class ProductNew extends BaseEntity {

    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Integer quantity;
    private String brand;
    private String unit; // e.g., "kg", "piece", "liter"
    private BigDecimal pua; // Packaged Unit A - Weight/Volume
    private BigDecimal pub; // Packaged Unit B - Weight/Volume  
    private BigDecimal puc; // Packaged Unit C - Weight/Volume
    private String itemUnit; // JSON structure for unit details
    private Boolean isActive;
    private Subcategory subcategory;

    public ProductNew() {
    }

    public ProductNew(String name, String description, BigDecimal price, String imageUrl, 
                     Integer quantity, String brand, String unit) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.brand = brand;
        this.unit = unit;
        this.isActive = true;
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

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "brand")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Column(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(name = "pua", precision = 10, scale = 2)
    public BigDecimal getPua() {
        return pua;
    }

    public void setPua(BigDecimal pua) {
        this.pua = pua;
    }

    @Column(name = "pub", precision = 10, scale = 2)
    public BigDecimal getPub() {
        return pub;
    }

    public void setPub(BigDecimal pub) {
        this.pub = pub;
    }

    @Column(name = "puc", precision = 10, scale = 2)
    public BigDecimal getPuc() {
        return puc;
    }

    public void setPuc(BigDecimal puc) {
        this.puc = puc;
    }

    @Column(name = "item_unit")
    @Type(type = "org.hibernate.type.TextType")
    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    @Column(name = "is_active", nullable = false)
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }
}