package org.softuni.onlinegrocery.service;

import org.modelmapper.ModelMapper;
import org.softuni.onlinegrocery.domain.entities.CategoryNew;
import org.softuni.onlinegrocery.domain.entities.Subcategory;
import org.softuni.onlinegrocery.domain.models.service.SubcategoryServiceModel;
import org.softuni.onlinegrocery.repository.CategoryNewRepository;
import org.softuni.onlinegrocery.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryNewRepository categoryNewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SubcategoryServiceImpl(SubcategoryRepository subcategoryRepository, 
                                 CategoryNewRepository categoryNewRepository, 
                                 ModelMapper modelMapper) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryNewRepository = categoryNewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SubcategoryServiceModel save(SubcategoryServiceModel subcategoryServiceModel) {
        Subcategory subcategory = modelMapper.map(subcategoryServiceModel, Subcategory.class);
        
        // Set the category reference
        if (subcategoryServiceModel.getCategoryNewId() != null) {
            CategoryNew categoryNew = categoryNewRepository.findById(subcategoryServiceModel.getCategoryNewId()).orElse(null);
            subcategory.setCategoryNew(categoryNew);
        }
        
        Subcategory savedSubcategory = subcategoryRepository.save(subcategory);
        SubcategoryServiceModel result = modelMapper.map(savedSubcategory, SubcategoryServiceModel.class);
        
        // Set additional fields
        if (savedSubcategory.getCategoryNew() != null) {
            result.setCategoryNewId(savedSubcategory.getCategoryNew().getId());
            result.setCategoryNewName(savedSubcategory.getCategoryNew().getName());
        }
        
        return result;
    }

    @Override
    public SubcategoryServiceModel findById(String id) {
        Subcategory subcategory = subcategoryRepository.findById(id).orElse(null);
        if (subcategory != null) {
            SubcategoryServiceModel result = modelMapper.map(subcategory, SubcategoryServiceModel.class);
            if (subcategory.getCategoryNew() != null) {
                result.setCategoryNewId(subcategory.getCategoryNew().getId());
                result.setCategoryNewName(subcategory.getCategoryNew().getName());
            }
            return result;
        }
        return null;
    }

    @Override
    public SubcategoryServiceModel findByName(String name) {
        Subcategory subcategory = subcategoryRepository.findByName(name).orElse(null);
        if (subcategory != null) {
            SubcategoryServiceModel result = modelMapper.map(subcategory, SubcategoryServiceModel.class);
            if (subcategory.getCategoryNew() != null) {
                result.setCategoryNewId(subcategory.getCategoryNew().getId());
                result.setCategoryNewName(subcategory.getCategoryNew().getName());
            }
            return result;
        }
        return null;
    }

    @Override
    public List<SubcategoryServiceModel> findByCategoryNewId(String categoryId) {
        return subcategoryRepository.findByCategoryNewIdOrderByName(categoryId)
                .stream()
                .map(subcategory -> {
                    SubcategoryServiceModel result = modelMapper.map(subcategory, SubcategoryServiceModel.class);
                    if (subcategory.getCategoryNew() != null) {
                        result.setCategoryNewId(subcategory.getCategoryNew().getId());
                        result.setCategoryNewName(subcategory.getCategoryNew().getName());
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SubcategoryServiceModel> findByCategoryNewName(String categoryName) {
        return subcategoryRepository.findByCategoryNewNameOrderByName(categoryName)
                .stream()
                .map(subcategory -> {
                    SubcategoryServiceModel result = modelMapper.map(subcategory, SubcategoryServiceModel.class);
                    if (subcategory.getCategoryNew() != null) {
                        result.setCategoryNewId(subcategory.getCategoryNew().getId());
                        result.setCategoryNewName(subcategory.getCategoryNew().getName());
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SubcategoryServiceModel findByIdWithProducts(String id) {
        Subcategory subcategory = subcategoryRepository.findByIdWithProducts(id).orElse(null);
        if (subcategory != null) {
            SubcategoryServiceModel result = modelMapper.map(subcategory, SubcategoryServiceModel.class);
            if (subcategory.getCategoryNew() != null) {
                result.setCategoryNewId(subcategory.getCategoryNew().getId());
                result.setCategoryNewName(subcategory.getCategoryNew().getName());
            }
            return result;
        }
        return null;
    }

    @Override
    public List<SubcategoryServiceModel> findByCategoryNewIdWithProducts(String categoryId) {
        return subcategoryRepository.findByCategoryNewIdWithProducts(categoryId)
                .stream()
                .map(subcategory -> {
                    SubcategoryServiceModel result = modelMapper.map(subcategory, SubcategoryServiceModel.class);
                    if (subcategory.getCategoryNew() != null) {
                        result.setCategoryNewId(subcategory.getCategoryNew().getId());
                        result.setCategoryNewName(subcategory.getCategoryNew().getName());
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SubcategoryServiceModel update(String id, SubcategoryServiceModel subcategoryServiceModel) {
        Subcategory existingSubcategory = subcategoryRepository.findById(id).orElse(null);
        if (existingSubcategory != null) {
            existingSubcategory.setName(subcategoryServiceModel.getName());
            existingSubcategory.setDescription(subcategoryServiceModel.getDescription());
            existingSubcategory.setImageUrl(subcategoryServiceModel.getImageUrl());
            
            // Update category reference if needed
            if (subcategoryServiceModel.getCategoryNewId() != null) {
                CategoryNew categoryNew = categoryNewRepository.findById(subcategoryServiceModel.getCategoryNewId()).orElse(null);
                existingSubcategory.setCategoryNew(categoryNew);
            }
            
            Subcategory updatedSubcategory = subcategoryRepository.save(existingSubcategory);
            SubcategoryServiceModel result = modelMapper.map(updatedSubcategory, SubcategoryServiceModel.class);
            
            if (updatedSubcategory.getCategoryNew() != null) {
                result.setCategoryNewId(updatedSubcategory.getCategoryNew().getId());
                result.setCategoryNewName(updatedSubcategory.getCategoryNew().getName());
            }
            
            return result;
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        subcategoryRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndCategoryNewId(String name, String categoryId) {
        return subcategoryRepository.existsByNameAndCategoryNewId(name, categoryId);
    }
}
