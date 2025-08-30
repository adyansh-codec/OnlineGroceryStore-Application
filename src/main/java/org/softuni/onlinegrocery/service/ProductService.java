package org.softuni.onlinegrocery.service;

import org.softuni.onlinegrocery.domain.models.service.ProductNewServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductNewServiceModel save(ProductNewServiceModel productNewServiceModel);

    ProductNewServiceModel findById(String id);

    List<ProductNewServiceModel> findBySubcategoryId(String subcategoryId);

    List<ProductNewServiceModel> findByCategoryNewId(String categoryId);

    List<ProductNewServiceModel> findAll();

    List<ProductNewServiceModel> findByNameContaining(String name);

    List<ProductNewServiceModel> findByBrand(String brand);

    List<ProductNewServiceModel> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductNewServiceModel> findInStock();

    ProductNewServiceModel update(String id, ProductNewServiceModel productNewServiceModel);

    void delete(String id);

    void activate(String id);

    void deactivate(String id);
}