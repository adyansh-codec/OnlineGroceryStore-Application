package org.softuni.onlinegrocery.service;

import org.softuni.onlinegrocery.domain.models.service.CategoryNewServiceModel;

import java.util.List;

public interface CategoryNewService {
    
    CategoryNewServiceModel save(CategoryNewServiceModel categoryNewServiceModel);
    
    CategoryNewServiceModel findById(String id);
    
    CategoryNewServiceModel findByName(String name);
    
    List<CategoryNewServiceModel> findAllOrderByName();
    
    List<CategoryNewServiceModel> findAllWithSubcategories();
    
    CategoryNewServiceModel update(String id, CategoryNewServiceModel categoryNewServiceModel);
    
    void deleteById(String id);
    
    boolean existsByName(String name);
}
