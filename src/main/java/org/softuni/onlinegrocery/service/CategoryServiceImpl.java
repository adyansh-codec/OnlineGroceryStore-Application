package org.softuni.onlinegrocery.service;

import org.modelmapper.ModelMapper;
import org.softuni.onlinegrocery.domain.entities.CategoryNew;
import org.softuni.onlinegrocery.domain.models.service.CategoryNewServiceModel;
import org.softuni.onlinegrocery.repository.CategoryNewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryNewRepository categoryNewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryNewRepository categoryNewRepository,
                               ModelMapper modelMapper) {
        this.categoryNewRepository = categoryNewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryNewServiceModel save(CategoryNewServiceModel categoryNewServiceModel) {
        CategoryNew categoryNew = modelMapper.map(categoryNewServiceModel, CategoryNew.class);
        CategoryNew savedCategory = categoryNewRepository.save(categoryNew);
        return modelMapper.map(savedCategory, CategoryNewServiceModel.class);
    }

    @Override
    public List<CategoryNewServiceModel> findAll() {
        return categoryNewRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryNewServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryNewServiceModel findById(String id) {
        CategoryNew categoryNew = categoryNewRepository.findById(id).orElse(null);
        if (categoryNew != null) {
            return modelMapper.map(categoryNew, CategoryNewServiceModel.class);
        }
        return null;
    }

    @Override
    public CategoryNewServiceModel update(String id, CategoryNewServiceModel categoryNewServiceModel) {
        CategoryNew existingCategory = categoryNewRepository.findById(id).orElse(null);
        if (existingCategory != null) {
            existingCategory.setName(categoryNewServiceModel.getName());
            existingCategory.setDescription(categoryNewServiceModel.getDescription());
            existingCategory.setImageUrl(categoryNewServiceModel.getImageUrl());
            
            CategoryNew savedCategory = categoryNewRepository.save(existingCategory);
            return modelMapper.map(savedCategory, CategoryNewServiceModel.class);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        CategoryNew categoryNew = categoryNewRepository.findById(id).orElse(null);
        if (categoryNew != null) {
            categoryNewRepository.delete(categoryNew);
        }
    }

    @Override
    public List<CategoryNewServiceModel> findAllActive() {
        return categoryNewRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryNewServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllCategoryNames() {
        return categoryNewRepository.findAll()
                .stream()
                .map(CategoryNew::getName)
                .collect(Collectors.toList());
    }
}