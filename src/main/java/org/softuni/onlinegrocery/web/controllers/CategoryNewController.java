package org.softuni.onlinegrocery.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.onlinegrocery.domain.models.service.CategoryNewServiceModel;
import org.softuni.onlinegrocery.domain.models.view.CategoryNewViewModel;
import org.softuni.onlinegrocery.service.CategoryNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories-new")
public class CategoryNewController extends BaseController {

    private final CategoryNewService categoryNewService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryNewController(CategoryNewService categoryNewService, ModelMapper modelMapper) {
        this.categoryNewService = categoryNewService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/fetch")
    @ResponseBody
    public List<CategoryNewViewModel> fetchCategories() {
        List<CategoryNewViewModel> categories =
                mapCategoryNewServiceToViewModel(categoryNewService.findAllOrderByName());

        return categories;
    }

    @GetMapping("/all/name")
    @ResponseBody
    public ResponseEntity<List<String>> allCategoryByName() {
        List<String> names = categoryNewService.findAllOrderByName()
                .stream()
                .map(CategoryNewServiceModel::getName)
                .collect(Collectors.toList());

        return ResponseEntity.ok(names);
    }

    private List<CategoryNewViewModel> mapCategoryNewServiceToViewModel
            (List<CategoryNewServiceModel> categoryNewServiceModels) {
        return categoryNewServiceModels.stream()
                .map(category -> modelMapper.map(category, CategoryNewViewModel.class))
                .collect(Collectors.toList());
    }
}
