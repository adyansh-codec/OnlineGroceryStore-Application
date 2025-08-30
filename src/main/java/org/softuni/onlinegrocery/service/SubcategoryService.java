package org.softuni.onlinegrocery.service;

import org.softuni.onlinegrocery.domain.models.service.SubcategoryServiceModel;

import java.util.List;

public interface SubcategoryService {
    
    SubcategoryServiceModel save(SubcategoryServiceModel subcategoryServiceModel);
    
    SubcategoryServiceModel findById(String id);
    
    SubcategoryServiceModel findByName(String name);
    
    List<SubcategoryServiceModel> findByCategoryNewId(String categoryId);
    
    List<SubcategoryServiceModel> findByCategoryNewName(String categoryName);
    
    SubcategoryServiceModel findByIdWithProducts(String id);
    
    List<SubcategoryServiceModel> findByCategoryNewIdWithProducts(String categoryId);
    
    SubcategoryServiceModel update(String id, SubcategoryServiceModel subcategoryServiceModel);
    
    void deleteById(String id);
    
    boolean existsByNameAndCategoryNewId(String name, String categoryId);
}
