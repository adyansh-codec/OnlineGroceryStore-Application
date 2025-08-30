package org.softuni.onlinegrocery.service;

import org.softuni.onlinegrocery.domain.models.service.CategoryNewServiceModel;

import java.util.List;

public interface CategoryService {

    CategoryNewServiceModel save(CategoryNewServiceModel categoryNewServiceModel);

    List<CategoryNewServiceModel> findAll();

    CategoryNewServiceModel findById(String id);

    CategoryNewServiceModel update(String id, CategoryNewServiceModel categoryNewServiceModel);

    void delete(String id);

    List<CategoryNewServiceModel> findAllActive();
    
    List<String> findAllCategoryNames();
}