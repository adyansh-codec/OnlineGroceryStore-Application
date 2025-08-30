package org.softuni.onlinegrocery.web.controllers;

import org.softuni.onlinegrocery.domain.models.service.CategoryNewServiceModel;
import org.softuni.onlinegrocery.domain.models.service.ProductNewServiceModel;
import org.softuni.onlinegrocery.domain.models.service.SubcategoryServiceModel;
import org.softuni.onlinegrocery.service.CategoryNewService;
import org.softuni.onlinegrocery.service.GoogleDriveService;
import org.softuni.onlinegrocery.service.ProductNewService;
import org.softuni.onlinegrocery.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/data-entry")
public class DataEntryController {

    private final CategoryNewService categoryNewService;
    private final SubcategoryService subcategoryService;
    private final ProductNewService productNewService;
    private final GoogleDriveService googleDriveService;

    @Autowired
    public DataEntryController(CategoryNewService categoryNewService,
                              SubcategoryService subcategoryService,
                              ProductNewService productNewService,
                              GoogleDriveService googleDriveService) {
        this.categoryNewService = categoryNewService;
        this.subcategoryService = subcategoryService;
        this.productNewService = productNewService;
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("")
    public ModelAndView dataEntry() {
        ModelAndView modelAndView = new ModelAndView("admin/data-entry");
        modelAndView.addObject("categories", categoryNewService.findAllOrderByName());
        modelAndView.addObject("googleDriveStatus", googleDriveService.getUploadInfo());
        modelAndView.addObject("googleDriveEnabled", googleDriveService.isEnabled());
        return modelAndView;
    }

    @GetMapping("/category/new")
    public ModelAndView newCategory() {
        ModelAndView modelAndView = new ModelAndView("admin/category-form");
        modelAndView.addObject("category", new CategoryNewServiceModel());
        modelAndView.addObject("isEdit", false);
        return modelAndView;
    }

    @PostMapping("/category/save")
    public String saveCategory(@ModelAttribute CategoryNewServiceModel category, 
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              Model model, RedirectAttributes redirectAttributes) {
        try {
            // Handle image upload if file is provided
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String fileName = "category_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                    String imageUrl = googleDriveService.uploadImage(imageFile, fileName);
                    category.setImageUrl(imageUrl);
                } catch (IOException e) {
                    model.addAttribute("error", "Error uploading image: " + e.getMessage());
                    model.addAttribute("category", category);
                    model.addAttribute("isEdit", category.getId() != null);
                    return "admin/category-form";
                }
            }

            if (category.getId() == null || category.getId().isEmpty()) {
                // Check if category name already exists
                if (categoryNewService.existsByName(category.getName())) {
                    model.addAttribute("error", "Category with this name already exists!");
                    model.addAttribute("category", category);
                    model.addAttribute("isEdit", false);
                    return "admin/category-form";
                }
                categoryNewService.save(category);
                redirectAttributes.addFlashAttribute("success", "Category created successfully!");
            } else {
                categoryNewService.update(category.getId(), category);
                redirectAttributes.addFlashAttribute("success", "Category updated successfully!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error saving category: " + e.getMessage());
            model.addAttribute("category", category);
            model.addAttribute("isEdit", category.getId() != null);
            return "admin/category-form";
        }
        return "redirect:/admin/data-entry";
    }

    @GetMapping("/category/edit/{id}")
    public ModelAndView editCategory(@PathVariable String id) {
        CategoryNewServiceModel category = categoryNewService.findById(id);
        if (category == null) {
            return new ModelAndView("redirect:/admin/data-entry");
        }
        ModelAndView modelAndView = new ModelAndView("admin/category-form");
        modelAndView.addObject("category", category);
        modelAndView.addObject("isEdit", true);
        return modelAndView;
    }

    @GetMapping("/subcategory/new")
    public ModelAndView newSubcategory(@RequestParam(required = false) String categoryId) {
        ModelAndView modelAndView = new ModelAndView("admin/subcategory-form");
        SubcategoryServiceModel subcategory = new SubcategoryServiceModel();
        if (categoryId != null) {
            subcategory.setCategoryNewId(categoryId);
        }
        modelAndView.addObject("subcategory", subcategory);
        modelAndView.addObject("categories", categoryNewService.findAllOrderByName());
        modelAndView.addObject("isEdit", false);
        return modelAndView;
    }

    @PostMapping("/subcategory/save")
    public String saveSubcategory(@ModelAttribute SubcategoryServiceModel subcategory,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                 Model model, RedirectAttributes redirectAttributes) {
        try {
            // Handle image upload if file is provided
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String fileName = "subcategory_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                    String imageUrl = googleDriveService.uploadImage(imageFile, fileName);
                    subcategory.setImageUrl(imageUrl);
                } catch (IOException e) {
                    model.addAttribute("error", "Error uploading image: " + e.getMessage());
                    model.addAttribute("subcategory", subcategory);
                    model.addAttribute("categories", categoryNewService.findAllOrderByName());
                    model.addAttribute("isEdit", subcategory.getId() != null);
                    return "admin/subcategory-form";
                }
            }

            if (subcategory.getId() == null || subcategory.getId().isEmpty()) {
                // Check if subcategory name already exists in this category
                if (subcategoryService.existsByNameAndCategoryNewId(subcategory.getName(), subcategory.getCategoryNewId())) {
                    model.addAttribute("error", "Subcategory with this name already exists in this category!");
                    model.addAttribute("subcategory", subcategory);
                    model.addAttribute("categories", categoryNewService.findAllOrderByName());
                    model.addAttribute("isEdit", false);
                    return "admin/subcategory-form";
                }
                subcategoryService.save(subcategory);
                redirectAttributes.addFlashAttribute("success", "Subcategory created successfully!");
            } else {
                subcategoryService.update(subcategory.getId(), subcategory);
                redirectAttributes.addFlashAttribute("success", "Subcategory updated successfully!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error saving subcategory: " + e.getMessage());
            model.addAttribute("subcategory", subcategory);
            model.addAttribute("categories", categoryNewService.findAllOrderByName());
            model.addAttribute("isEdit", subcategory.getId() != null);
            return "admin/subcategory-form";
        }
        return "redirect:/admin/data-entry";
    }

    @GetMapping("/subcategory/edit/{id}")
    public ModelAndView editSubcategory(@PathVariable String id) {
        SubcategoryServiceModel subcategory = subcategoryService.findById(id);
        if (subcategory == null) {
            return new ModelAndView("redirect:/admin/data-entry");
        }
        ModelAndView modelAndView = new ModelAndView("admin/subcategory-form");
        modelAndView.addObject("subcategory", subcategory);
        modelAndView.addObject("categories", categoryNewService.findAllOrderByName());
        modelAndView.addObject("isEdit", true);
        return modelAndView;
    }

    @GetMapping("/product/new")
    public ModelAndView newProduct(@RequestParam(required = false) String subcategoryId) {
        ModelAndView modelAndView = new ModelAndView("admin/product-form");
        ProductNewServiceModel product = new ProductNewServiceModel();
        if (subcategoryId != null) {
            product.setSubcategoryId(subcategoryId);
        }
        product.setIsActive(true);
        product.setQuantity(0);
        product.setPrice(BigDecimal.ZERO);
        
        modelAndView.addObject("product", product);
        modelAndView.addObject("categories", categoryNewService.findAllOrderByName());
        modelAndView.addObject("isEdit", false);
        return modelAndView;
    }

    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute ProductNewServiceModel product,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             Model model, RedirectAttributes redirectAttributes) {
        try {
            // Handle image upload if file is provided
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String fileName = "product_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                    String imageUrl = googleDriveService.uploadImage(imageFile, fileName);
                    product.setImageUrl(imageUrl);
                } catch (IOException e) {
                    model.addAttribute("error", "Error uploading image: " + e.getMessage());
                    model.addAttribute("product", product);
                    model.addAttribute("categories", categoryNewService.findAllOrderByName());
                    model.addAttribute("isEdit", product.getId() != null);
                    return "admin/product-form";
                }
            }

            if (product.getId() == null || product.getId().isEmpty()) {
                // Check if product name already exists in this subcategory
                if (productNewService.existsByNameAndSubcategoryId(product.getName(), product.getSubcategoryId())) {
                    model.addAttribute("error", "Product with this name already exists in this subcategory!");
                    model.addAttribute("product", product);
                    model.addAttribute("categories", categoryNewService.findAllOrderByName());
                    model.addAttribute("isEdit", false);
                    return "admin/product-form";
                }
                productNewService.save(product);
                redirectAttributes.addFlashAttribute("success", "Product created successfully!");
            } else {
                productNewService.update(product.getId(), product);
                redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error saving product: " + e.getMessage());
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryNewService.findAllOrderByName());
            model.addAttribute("isEdit", product.getId() != null);
            return "admin/product-form";
        }
        return "redirect:/admin/data-entry";
    }

    @GetMapping("/product/edit/{id}")
    public ModelAndView editProduct(@PathVariable String id) {
        ProductNewServiceModel product = productNewService.findById(id);
        if (product == null) {
            return new ModelAndView("redirect:/admin/data-entry");
        }
        ModelAndView modelAndView = new ModelAndView("admin/product-form");
        modelAndView.addObject("product", product);
        modelAndView.addObject("categories", categoryNewService.findAllOrderByName());
        modelAndView.addObject("isEdit", true);
        return modelAndView;
    }

    // AJAX endpoints for dynamic dropdowns
    @GetMapping("/api/subcategories/{categoryId}")
    @ResponseBody
    public List<SubcategoryServiceModel> getSubcategoriesByCategory(@PathVariable String categoryId) {
        return subcategoryService.findByCategoryNewId(categoryId);
    }

    @GetMapping("/api/products/{subcategoryId}")
    @ResponseBody
    public List<ProductNewServiceModel> getProductsBySubcategory(@PathVariable String subcategoryId) {
        return productNewService.findBySubcategoryId(subcategoryId);
    }

    // Delete endpoints
    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable String id, Model model) {
        try {
            categoryNewService.deleteById(id);
            model.addAttribute("success", "Category deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting category: " + e.getMessage());
        }
        return "redirect:/admin/data-entry";
    }

    @PostMapping("/subcategory/delete/{id}")
    public String deleteSubcategory(@PathVariable String id, Model model) {
        try {
            subcategoryService.deleteById(id);
            model.addAttribute("success", "Subcategory deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting subcategory: " + e.getMessage());
        }
        return "redirect:/admin/data-entry";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable String id, Model model) {
        try {
            productNewService.deleteById(id);
            model.addAttribute("success", "Product deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/admin/data-entry";
    }
}
