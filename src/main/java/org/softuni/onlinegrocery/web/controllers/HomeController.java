package org.softuni.onlinegrocery.web.controllers;

import org.modelmapper.ModelMapper;

import org.softuni.onlinegrocery.domain.models.service.CategoryNewServiceModel;
import org.softuni.onlinegrocery.domain.models.service.SubcategoryServiceModel;
import org.softuni.onlinegrocery.domain.models.service.ProductNewServiceModel;
import org.softuni.onlinegrocery.domain.models.view.CategoryNewViewModel;
import org.softuni.onlinegrocery.domain.models.view.SubcategoryViewModel;
import org.softuni.onlinegrocery.domain.models.view.ProductNewViewModel;
import org.softuni.onlinegrocery.service.CategoryNewService;
import org.softuni.onlinegrocery.service.SubcategoryService;
import org.softuni.onlinegrocery.service.ProductNewService;

import org.softuni.onlinegrocery.web.annotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.softuni.onlinegrocery.util.constants.AppConstants.*;

@Controller
public class HomeController extends BaseController {

    private final CategoryNewService categoryNewService;
    private final SubcategoryService subcategoryService;
    private final ProductNewService productNewService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(CategoryNewService categoryNewService, 
                         SubcategoryService subcategoryService, ProductNewService productNewService, 
                         ModelMapper modelMapper) {

        this.categoryNewService = categoryNewService;
        this.subcategoryService = subcategoryService;
        this.productNewService = productNewService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @PageTitle(INDEX)
    public ModelAndView renderIndexPage(Principal principal, ModelAndView modelAndView) {

        List<CategoryNewViewModel> categories =
                mapCategoryNewServiceToViewModel(categoryNewService.findAllWithSubcategories());

        modelAndView.addObject(PRINCIPAL_TO_LOWER_CASE, principal);
        modelAndView.addObject("username", principal != null ? principal.getName() : "Guest");
        modelAndView.addObject("greeting", "Welcome to Online Grocery Store");
        modelAndView.addObject(CATEGORIES_TO_LOWER_CASE, categories);

        return view("home", modelAndView);
    }

    @GetMapping("/home")
    //@PreAuthorize("isAuthenticated()")
    @PageTitle(HOME)
    public ModelAndView renderHomePage(Principal principal, ModelAndView modelAndView) {
        
        List<CategoryNewViewModel> categories =
                mapCategoryNewServiceToViewModel(categoryNewService.findAllWithSubcategories());
        
        modelAndView.addObject(PRINCIPAL_TO_LOWER_CASE, principal);
        modelAndView.addObject("username", principal != null ? principal.getName() : "Guest");
        modelAndView.addObject("greeting", "Welcome to Online Grocery Store");

        modelAndView.addObject(CATEGORIES_TO_LOWER_CASE, categories);

        return view("home", modelAndView);
    }

    private List<CategoryNewViewModel> mapCategoryNewServiceToViewModel(List<CategoryNewServiceModel> categoryNewServiceModels){
        return categoryNewServiceModels.stream()
                .map(category -> {
                    CategoryNewViewModel categoryView = modelMapper.map(category, CategoryNewViewModel.class);
                    
                    // Map subcategories and calculate product counts
                    if (category.getSubcategories() != null) {
                        List<SubcategoryViewModel> subcategoryViews = category.getSubcategories().stream()
                                .map(subcategory -> {
                                    SubcategoryViewModel subcategoryView = modelMapper.map(subcategory, SubcategoryViewModel.class);
                                    // Count products by subcategory ID using the product service
                                    int productCount = productNewService.findBySubcategoryId(subcategory.getId()).size();
                                    subcategoryView.setProductCount(productCount);
                                    return subcategoryView;
                                })
                                .collect(Collectors.toList());
                        categoryView.setSubcategories(subcategoryViews);
                    }
                    
                    return categoryView;
                })
                .collect(Collectors.toList());
    }

    /**
     * REST endpoint to fetch categories with subcategories for AJAX requests
     */
    @GetMapping("/api/categories/with-subcategories")
    @ResponseBody
    public ResponseEntity<List<CategoryNewViewModel>> getCategoriesWithSubcategories() {
        List<CategoryNewViewModel> categories = 
                mapCategoryNewServiceToViewModel(categoryNewService.findAllWithSubcategories());
        return ResponseEntity.ok(categories);
    }

    /**
     * REST endpoint to fetch subcategories by category name for lazy loading
     */
    @GetMapping("/api/subcategories/category/{categoryName}")
    @ResponseBody
    public ResponseEntity<List<SubcategoryViewModel>> getSubcategoriesByCategory(@PathVariable String categoryName) {
        List<SubcategoryServiceModel> subcategories = subcategoryService.findByCategoryNewName(categoryName);
        List<SubcategoryViewModel> subcategoryViewModels = subcategories.stream()
                .map(subcategory -> modelMapper.map(subcategory, SubcategoryViewModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(subcategoryViewModels);
    }

    /**
     * REST endpoint to fetch products by subcategory for lazy loading
     */
    @GetMapping("/api/products/subcategory/{subcategoryId}")
    @ResponseBody
    public ResponseEntity<List<ProductNewViewModel>> getProductsBySubcategory(@PathVariable String subcategoryId) {
        List<ProductNewServiceModel> products = productNewService.findBySubcategoryId(subcategoryId);
        List<ProductNewViewModel> productViewModels = products.stream()
                .map(product -> modelMapper.map(product, ProductNewViewModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productViewModels);
    }

    /**
     * REST endpoint to fetch products by category for lazy loading
     */
    @GetMapping("/api/products/category/{categoryId}")
    @ResponseBody
    public ResponseEntity<List<ProductNewViewModel>> getProductsByCategory(@PathVariable String categoryId) {
        List<ProductNewServiceModel> products = productNewService.findByCategoryNewId(categoryId);
        List<ProductNewViewModel> productViewModels = products.stream()
                .map(product -> modelMapper.map(product, ProductNewViewModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productViewModels);
    }

    /**
     * Products page showing all products for a specific subcategory
     */
    @GetMapping("/products")
    @PageTitle("Products")
    public ModelAndView renderProductsPage(@RequestParam(required = false) String subcategoryId,
                                         @RequestParam(required = false) String subcategoryName,
                                         Principal principal, ModelAndView modelAndView) {
        
        List<ProductNewViewModel> products;
        String pageTitle = "All Products";
        
        if (subcategoryId != null && !subcategoryId.isEmpty()) {
            products = productNewService.findBySubcategoryId(subcategoryId)
                    .stream()
                    .map(product -> modelMapper.map(product, ProductNewViewModel.class))
                    .collect(Collectors.toList());
            pageTitle = subcategoryName != null ? subcategoryName + " Products" : "Products";
        } else {
            products = productNewService.findAll()
                    .stream()
                    .map(product -> modelMapper.map(product, ProductNewViewModel.class))
                    .collect(Collectors.toList());
        }
        
        modelAndView.addObject("products", products);
        modelAndView.addObject("pageTitle", pageTitle);
        modelAndView.addObject("subcategoryName", subcategoryName);
        modelAndView.addObject(PRINCIPAL_TO_LOWER_CASE, principal);
        modelAndView.addObject("username", principal != null ? principal.getName() : "Guest");

        return view("products", modelAndView);
    }

    /**
     * Entry page for products with enhanced UI - opens in new tab
     */
    @GetMapping("/entry")
    @PageTitle("Product Entry")
    public ModelAndView renderEntryPage(@RequestParam(required = false) String subcategoryId,
                                       @RequestParam(required = false) String subcategoryName,
                                       Principal principal, ModelAndView modelAndView) {
        
        List<ProductNewViewModel> products;
        String pageTitle = "Product Explorer";
        
        if (subcategoryId != null && !subcategoryId.isEmpty()) {
            products = productNewService.findBySubcategoryId(subcategoryId)
                    .stream()
                    .map(product -> modelMapper.map(product, ProductNewViewModel.class))
                    .collect(Collectors.toList());
            pageTitle = subcategoryName != null ? subcategoryName + " - Product Entry" : "Product Entry";
        } else {
            products = productNewService.findAll()
                    .stream()
                    .map(product -> modelMapper.map(product, ProductNewViewModel.class))
                    .collect(Collectors.toList());
        }
        
        modelAndView.addObject("products", products);
        modelAndView.addObject("pageTitle", pageTitle);
        modelAndView.addObject("subcategoryName", subcategoryName);
        modelAndView.addObject(PRINCIPAL_TO_LOWER_CASE, principal);
        modelAndView.addObject("username", principal != null ? principal.getName() : "Guest");

        return view("entry", modelAndView);
    }
}
