package org.softuni.onlinegrocery.service;

import org.softuni.onlinegrocery.domain.models.service.ProductNewServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductNewService {
    
    ProductNewServiceModel save(ProductNewServiceModel productNewServiceModel);
    
    ProductNewServiceModel findById(String id);
    
    List<ProductNewServiceModel> findBySubcategoryId(String subcategoryId);
    
    List<ProductNewServiceModel> findAll();
    
    List<ProductNewServiceModel> findByCategoryNewId(String categoryId);
    
    List<ProductNewServiceModel> findByNameContaining(String name);
    
    List<ProductNewServiceModel> findByBrand(String brand);
    
    List<ProductNewServiceModel> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<ProductNewServiceModel> findAvailableProducts();
    
    List<String> findDistinctBrands();
    
    ProductNewServiceModel findByNameAndSubcategoryId(String name, String subcategoryId);
    
    ProductNewServiceModel update(String id, ProductNewServiceModel productNewServiceModel);
    
    void deleteById(String id);
    
    boolean existsByNameAndSubcategoryId(String name, String subcategoryId);
}
