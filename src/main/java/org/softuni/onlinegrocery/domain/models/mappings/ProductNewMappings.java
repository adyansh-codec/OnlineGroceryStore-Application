package org.softuni.onlinegrocery.domain.models.mappings;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.softuni.onlinegrocery.domain.entities.ProductNew;
import org.softuni.onlinegrocery.domain.models.service.ProductNewServiceModel;
import org.softuni.onlinegrocery.util.mappings.IHaveCustomMappings;

public class ProductNewMappings implements IHaveCustomMappings {

    @Override
    public void configureMappings(ModelMapper mapper) {
        // Configure mapping from ProductNew entity to ProductNewServiceModel
        mapper.addMappings(new PropertyMap<ProductNew, ProductNewServiceModel>() {
            @Override
            protected void configure() {
                // Map all basic fields automatically
                map().setId(source.getId());
                map().setName(source.getName());
                map().setDescription(source.getDescription());
                map().setPrice(source.getPrice());
                map().setImageUrl(source.getImageUrl());
                map().setQuantity(source.getQuantity());
                map().setBrand(source.getBrand());
                map().setUnit(source.getUnit());
                map().setPua(source.getPua());
                map().setPub(source.getPub());
                map().setPuc(source.getPuc());
                map().setItemUnit(source.getItemUnit());
                map().setIsActive(source.getIsActive());
                
                // Skip complex relationships - these will be handled manually in service
                skip().setSubcategoryId(null);
                skip().setSubcategoryName(null);
                skip().setCategoryNewId(null);
                skip().setCategoryNewName(null);
            }
        });

        // Configure mapping from ProductNewServiceModel to ProductNew entity
        mapper.addMappings(new PropertyMap<ProductNewServiceModel, ProductNew>() {
            @Override
            protected void configure() {
                // Map all basic fields automatically
                map().setId(source.getId());
                map().setName(source.getName());
                map().setDescription(source.getDescription());
                map().setPrice(source.getPrice());
                map().setImageUrl(source.getImageUrl());
                map().setQuantity(source.getQuantity());
                map().setBrand(source.getBrand());
                map().setUnit(source.getUnit());
                map().setPua(source.getPua());
                map().setPub(source.getPub());
                map().setPuc(source.getPuc());
                map().setItemUnit(source.getItemUnit());
                map().setIsActive(source.getIsActive());
                
                // Skip the subcategory relationship - handled manually in service
                skip().setSubcategory(null);
            }
        });
    }
}