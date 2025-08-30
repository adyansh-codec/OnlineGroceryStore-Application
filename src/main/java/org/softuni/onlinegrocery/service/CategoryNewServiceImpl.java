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
public class CategoryNewServiceImpl implements CategoryNewService {

    private final CategoryNewRepository categoryNewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryNewServiceImpl(CategoryNewRepository categoryNewRepository, ModelMapper modelMapper) {
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
    public CategoryNewServiceModel findById(String id) {
        CategoryNew categoryNew = categoryNewRepository.findById(id).orElse(null);
        return categoryNew != null ? modelMapper.map(categoryNew, CategoryNewServiceModel.class) : null;
    }

    @Override
    public CategoryNewServiceModel findByName(String name) {
        CategoryNew categoryNew = categoryNewRepository.findByName(name).orElse(null);
        return categoryNew != null ? modelMapper.map(categoryNew, CategoryNewServiceModel.class) : null;
    }

    @Override
    public List<CategoryNewServiceModel> findAllOrderByName() {
        return categoryNewRepository.findAllOrderByName()
                .stream()
                .map(category -> modelMapper.map(category, CategoryNewServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryNewServiceModel> findAllWithSubcategories() {
        return categoryNewRepository.findAllWithSubcategories()
                .stream()
                .map(category -> modelMapper.map(category, CategoryNewServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryNewServiceModel update(String id, CategoryNewServiceModel categoryNewServiceModel) {
        CategoryNew existingCategory = categoryNewRepository.findById(id).orElse(null);
        if (existingCategory != null) {
            existingCategory.setName(categoryNewServiceModel.getName());
            existingCategory.setDescription(categoryNewServiceModel.getDescription());
            existingCategory.setImageUrl(categoryNewServiceModel.getImageUrl());
            CategoryNew updatedCategory = categoryNewRepository.save(existingCategory);
            return modelMapper.map(updatedCategory, CategoryNewServiceModel.class);
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        categoryNewRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryNewRepository.existsByName(name);
    }
}
