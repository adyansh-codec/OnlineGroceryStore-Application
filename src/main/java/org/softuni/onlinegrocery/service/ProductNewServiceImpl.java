package org.softuni.onlinegrocery.service;

import org.modelmapper.ModelMapper;
import org.softuni.onlinegrocery.domain.entities.ProductNew;
import org.softuni.onlinegrocery.domain.entities.Subcategory;
import org.softuni.onlinegrocery.domain.models.service.ProductNewServiceModel;
import org.softuni.onlinegrocery.repository.ProductNewRepository;
import org.softuni.onlinegrocery.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductNewServiceImpl implements ProductNewService {

    private final ProductNewRepository productNewRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductNewServiceImpl(ProductNewRepository productNewRepository, 
                                SubcategoryRepository subcategoryRepository, 
                                ModelMapper modelMapper) {
        this.productNewRepository = productNewRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductNewServiceModel save(ProductNewServiceModel productNewServiceModel) {
        ProductNew productNew = modelMapper.map(productNewServiceModel, ProductNew.class);
        
        // Set the subcategory reference
        if (productNewServiceModel.getSubcategoryId() != null) {
            Subcategory subcategory = subcategoryRepository.findById(productNewServiceModel.getSubcategoryId()).orElse(null);
            productNew.setSubcategory(subcategory);
        }
        
        ProductNew savedProduct = productNewRepository.save(productNew);
        ProductNewServiceModel result = modelMapper.map(savedProduct, ProductNewServiceModel.class);
        
        // Set additional fields
        if (savedProduct.getSubcategory() != null) {
            result.setSubcategoryId(savedProduct.getSubcategory().getId());
            result.setSubcategoryName(savedProduct.getSubcategory().getName());
            if (savedProduct.getSubcategory().getCategoryNew() != null) {
                result.setCategoryNewId(savedProduct.getSubcategory().getCategoryNew().getId());
                result.setCategoryNewName(savedProduct.getSubcategory().getCategoryNew().getName());
            }
        }
        
        return result;
    }

    @Override
    public ProductNewServiceModel findById(String id) {
        ProductNew productNew = productNewRepository.findById(id).orElse(null);
        if (productNew != null) {
            ProductNewServiceModel result = modelMapper.map(productNew, ProductNewServiceModel.class);
            if (productNew.getSubcategory() != null) {
                result.setSubcategoryId(productNew.getSubcategory().getId());
                result.setSubcategoryName(productNew.getSubcategory().getName());
                if (productNew.getSubcategory().getCategoryNew() != null) {
                    result.setCategoryNewId(productNew.getSubcategory().getCategoryNew().getId());
                    result.setCategoryNewName(productNew.getSubcategory().getCategoryNew().getName());
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public List<ProductNewServiceModel> findBySubcategoryId(String subcategoryId) {
        return productNewRepository.findBySubcategoryIdAndIsActiveTrueOrderByName(subcategoryId)
                .stream()
                .map(this::mapToServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductNewServiceModel> findAll() {
        return productNewRepository.findByIsActiveTrueOrderByName()
                .stream()
                .map(this::mapToServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductNewServiceModel> findByCategoryNewId(String categoryId) {
        return productNewRepository.findByCategoryNewIdAndIsActiveTrueOrderByName(categoryId)
                .stream()
                .map(this::mapToServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductNewServiceModel> findByNameContaining(String name) {
        return productNewRepository.findByNameContainingAndIsActiveTrueOrderByName(name)
                .stream()
                .map(this::mapToServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductNewServiceModel> findByBrand(String brand) {
        return productNewRepository.findByBrandAndIsActiveTrueOrderByName(brand)
                .stream()
                .map(this::mapToServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductNewServiceModel> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return productNewRepository.findByPriceBetweenAndIsActiveTrueOrderByPrice(minPrice, maxPrice)
                .stream()
                .map(this::mapToServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductNewServiceModel> findAvailableProducts() {
        return productNewRepository.findAvailableProductsOrderByName()
                .stream()
                .map(this::mapToServiceModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findDistinctBrands() {
        return productNewRepository.findDistinctBrands();
    }

    @Override
    public ProductNewServiceModel findByNameAndSubcategoryId(String name, String subcategoryId) {
        ProductNew productNew = productNewRepository.findByNameAndSubcategoryId(name, subcategoryId).orElse(null);
        return productNew != null ? mapToServiceModel(productNew) : null;
    }

    @Override
    public ProductNewServiceModel update(String id, ProductNewServiceModel productNewServiceModel) {
        ProductNew existingProduct = productNewRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(productNewServiceModel.getName());
            existingProduct.setDescription(productNewServiceModel.getDescription());
            existingProduct.setPrice(productNewServiceModel.getPrice());
            existingProduct.setImageUrl(productNewServiceModel.getImageUrl());
            existingProduct.setQuantity(productNewServiceModel.getQuantity());
            existingProduct.setBrand(productNewServiceModel.getBrand());
            existingProduct.setUnit(productNewServiceModel.getUnit());
            existingProduct.setPua(productNewServiceModel.getPua());
            existingProduct.setPub(productNewServiceModel.getPub());
            existingProduct.setPuc(productNewServiceModel.getPuc());
            existingProduct.setItemUnit(productNewServiceModel.getItemUnit());
            existingProduct.setIsActive(productNewServiceModel.getIsActive());
            
            // Update subcategory reference if needed
            if (productNewServiceModel.getSubcategoryId() != null) {
                Subcategory subcategory = subcategoryRepository.findById(productNewServiceModel.getSubcategoryId()).orElse(null);
                existingProduct.setSubcategory(subcategory);
            }
            
            ProductNew updatedProduct = productNewRepository.save(existingProduct);
            return mapToServiceModel(updatedProduct);
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        productNewRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndSubcategoryId(String name, String subcategoryId) {
        return productNewRepository.existsByNameAndSubcategoryId(name, subcategoryId);
    }

    private ProductNewServiceModel mapToServiceModel(ProductNew productNew) {
        ProductNewServiceModel result = modelMapper.map(productNew, ProductNewServiceModel.class);
        if (productNew.getSubcategory() != null) {
            result.setSubcategoryId(productNew.getSubcategory().getId());
            result.setSubcategoryName(productNew.getSubcategory().getName());
            if (productNew.getSubcategory().getCategoryNew() != null) {
                result.setCategoryNewId(productNew.getSubcategory().getCategoryNew().getId());
                result.setCategoryNewName(productNew.getSubcategory().getCategoryNew().getName());
            }
        }
        return result;
    }
}