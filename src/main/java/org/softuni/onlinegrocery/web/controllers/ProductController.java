package org.softuni.onlinegrocery.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.onlinegrocery.domain.models.binding.ProductAddBindingModel;
import org.softuni.onlinegrocery.domain.models.service.CategoryNewServiceModel;
import org.softuni.onlinegrocery.domain.models.service.ProductNewServiceModel;
import org.softuni.onlinegrocery.domain.models.view.CategoryViewModel;
import org.softuni.onlinegrocery.domain.models.view.ProductAllViewModel;
import org.softuni.onlinegrocery.domain.models.view.ProductDetailsViewModel;
import org.softuni.onlinegrocery.util.error.ProductNameAlreadyExistsException;
import org.softuni.onlinegrocery.util.error.ProductNotFoundException;
import org.softuni.onlinegrocery.service.*;
import org.softuni.onlinegrocery.web.annotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.softuni.onlinegrocery.util.constants.AppConstants.*;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, UserService userService,
                             CloudinaryService cloudinaryService, CategoryService categoryService,
                             ModelMapper modelMapper) {
        this.productService = productService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
   // @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Add Products")
    public ModelAndView addProduct(@ModelAttribute(name = MODEL) ProductAddBindingModel model,
                                   ModelAndView modelAndView) {
        return loadAndReturnModelAndView(model, modelAndView);
    }

    @PostMapping("/add")
   // @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addProductConfirm(@Valid @ModelAttribute(name = MODEL) ProductAddBindingModel model,
                                          BindingResult bindingResult,
                                          ModelAndView modelAndView) throws IOException {
        ProductNewServiceModel productServiceModel = modelMapper.map(model, ProductNewServiceModel.class);

        if (bindingResult.hasErrors() || model.getImage().isEmpty() ||
                productService.save(productServiceModel) == null) {

            return loadAndReturnModelAndView(model, modelAndView);
        }
        return redirect("/products/add");
    }

    @GetMapping("/all")
   // @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Products")
    public ModelAndView allProducts(ModelAndView modelAndView) {

        List<ProductAllViewModel> productAllViewModels =
                mapProductServiceToViewModel(productService.findAll());

        modelAndView.addObject(PRODUCTS_TO_LOWERCASE, productAllViewModels);

        return view("product/all-products", modelAndView);
    }
    
    @GetMapping("/all/name")
    // @PreAuthorize("hasRole('ROLE_MODERATOR')")
     @PageTitle("Products")
     public ResponseEntity<List<String>> allProductsByName(ModelAndView modelAndView) {

         List<String> names = productService.findAll().stream()
                .map(ProductNewServiceModel::getName)
                .collect(Collectors.toList());

         return ResponseEntity.ok(names);
     }

    @GetMapping("/details/{id}")
    //@PreAuthorize("isAuthenticated()")
    @PageTitle("Product Details")
    public ModelAndView detailsProduct(@PathVariable String id, ModelAndView modelAndView) {
        ProductDetailsViewModel product =
                modelMapper.map(productService.findById(id), ProductDetailsViewModel.class);
        modelAndView.addObject(PRODUCT_TO_LOWERCASE, product);

        return view("product/details", modelAndView);
    }

    @GetMapping("/{id}")
    @PageTitle("Product Details")
    public ModelAndView productById(@PathVariable String id, ModelAndView modelAndView) {
        try {
            // Debug logging
            System.out.println("Attempting to find product with ID: " + id);
            
            ProductNewServiceModel productServiceModel = productService.findById(id);
            
            if (productServiceModel == null) {
                System.out.println("Product not found with ID: " + id);
                modelAndView.addObject("error", "Product not found with ID: " + id);
                modelAndView.addObject("pageTitle", "Product Not Found");
                return view("error", modelAndView);
            }
            
            // Debug logging
            System.out.println("Found product: " + productServiceModel.getName() + " with price: " + productServiceModel.getPrice());
            System.out.println("Product ID from service model: " + productServiceModel.getId());
            
            ProductDetailsViewModel product = modelMapper.map(productServiceModel, ProductDetailsViewModel.class);
            
            // Ensure ID is properly mapped - THIS IS THE KEY FIX
            if (product.getId() == null) {
                product.setId(productServiceModel.getId());
            }
            
            // Ensure price is properly mapped
            if (product.getPrice() == null && productServiceModel.getPrice() != null) {
                product.setPrice(productServiceModel.getPrice());
            }
            
            // Map itemUnit field for variants functionality
            if (productServiceModel.getItemUnit() != null) {
                System.out.println("DEBUG: Found itemUnit data in service model:");
                System.out.println("DEBUG: itemUnit content: " + productServiceModel.getItemUnit());
                System.out.println("DEBUG: itemUnit length: " + productServiceModel.getItemUnit().length());
                product.setItemUnit(productServiceModel.getItemUnit());
            } else {
                System.out.println("DEBUG: No itemUnit data found in service model for product: " + productServiceModel.getName());
            }
            
            // Map category and subcategory information for breadcrumb
            if (productServiceModel.getCategoryNewId() != null) {
                product.setCategoryId(productServiceModel.getCategoryNewId());
                product.setCategoryName(productServiceModel.getCategoryNewName());
            }
            if (productServiceModel.getSubcategoryId() != null) {
                product.setSubcategoryId(productServiceModel.getSubcategoryId());
                product.setSubcategoryName(productServiceModel.getSubcategoryName());
            }
            
            // Debug logging
            System.out.println("Mapped product: " + product.getName() + " with price: " + product.getPrice());
            System.out.println("Mapped product ID: " + product.getId()); // Add this debug line
            System.out.println("Category: " + product.getCategoryName() + " (" + product.getCategoryId() + ")");
            System.out.println("Subcategory: " + product.getSubcategoryName() + " (" + product.getSubcategoryId() + ")");
            System.out.println("DEBUG: Final itemUnit in view model: " + (product.getItemUnit() != null ? product.getItemUnit() : "NULL"));
            
            modelAndView.addObject(PRODUCT_TO_LOWERCASE, product);
            
            return view("productdesc", modelAndView);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error loading product with ID: " + id + " - " + e.getMessage());
            e.printStackTrace();
            
            // Show proper error page instead of productdesc with error
            modelAndView.addObject("error", "Error loading product: " + e.getMessage());
            modelAndView.addObject("pageTitle", "Error Loading Product");
            return view("error", modelAndView);
        }
    }

    @GetMapping("/edit/{id}")
   // @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Edit Product")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView modelAndView,
                                    @ModelAttribute(MODEL) ProductAddBindingModel productAddBindingModel) {

        productAddBindingModel =
                this.modelMapper.map(productService.findById(id), ProductAddBindingModel.class);

        List<CategoryViewModel> categoryViewModelList =
                mapCategoryServiceToViewModel(categoryService.findAll());

        modelAndView.addObject(MODEL, productAddBindingModel);

        modelAndView.addObject(CATEGORIES_TO_LOWER_CASE, categoryViewModelList);

        modelAndView.addObject(PRODUCT_ID, id);

        return view("product/edit-product", modelAndView);
    }

    @PostMapping("/edit/{id}")
   // @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editProductConfirm(ModelAndView modelAndView, @PathVariable String id,
                                           @Valid @ModelAttribute(MODEL) ProductAddBindingModel model,
                                           BindingResult bindingResult) throws IOException {

        ProductNewServiceModel productServiceModel = modelMapper.map(model, ProductNewServiceModel.class);

        productService.update(id, productServiceModel);

        if (bindingResult.hasErrors() ||
                productService.update(id, productServiceModel) == null) {
            modelAndView.addObject(CATEGORIES_TO_LOWER_CASE,
                    mapCategoryServiceToViewModel(categoryService.findAll()));
            modelAndView.addObject(MODEL, model);
            modelAndView.addObject(PRODUCT_ID, id);
            return this.view("product/edit-product", modelAndView);
        }

        return redirect("/products/details/" + id);
    }

    private void initImage(ProductAddBindingModel model) {
        if (model.getImage().isEmpty()){
            MultipartFile multipartFile = new MultipartFile() {
                @Override
                public String getName() {
                    return null;
                }

                @Override
                public String getOriginalFilename() {
                    return null;
                }

                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public long getSize() {
                    return ZERO_NUMBER;
                }

                @Override
                public byte[] getBytes() throws IOException {
                    return new byte[ZERO_NUMBER];
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return null;
                }

                @Override
                public void transferTo(File file) throws IOException, IllegalStateException {

                }
            };
            model.setImage(multipartFile);
        }
    }


    @GetMapping("/delete/{id}")
   // @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Delete Product")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView modelAndView,
                                      @ModelAttribute("model") ProductAddBindingModel productAddBindingModel) {

        productAddBindingModel = this.modelMapper.map(productService.findById(id), ProductAddBindingModel.class);

        modelAndView.addObject(MODEL, productAddBindingModel);

        modelAndView.addObject(CATEGORIES_TO_LOWER_CASE,
                mapCategoryServiceToViewModel(categoryService.findAll()));

        modelAndView.addObject(PRODUCT_ID, id);

        return view("product/delete-product", modelAndView);
    }

    @PostMapping("/delete/{id}")
   // @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteProductConfirm(@PathVariable String id) {

        productService.delete(id);

        return redirect("/products/all");
    }

    @GetMapping("/category/{category}")
    //@PreAuthorize("isAuthenticated()")
    public ModelAndView fetchByCategory(@PathVariable String category, ModelAndView modelAndView) {

        List<ProductAllViewModel> products = new ArrayList<>();

        products = category.equals("All")? mapProductServiceToViewModel(productService.findAll())
                : mapProductServiceToViewModel(productService.findByCategoryNewId(category));

        modelAndView.addObject(CATEGORY_NAME, category);

        modelAndView.addObject(PRODUCTS_TO_LOWERCASE, products);

        return view("product/show-products", modelAndView);
    }

    @GetMapping("/fetch")
    @ResponseBody
    public List<ProductAllViewModel> fetchAllProducts() {

        return mapProductServiceToViewModel(productService.findAll());

    }

    @GetMapping("/api/find")
    @ResponseBody
   // @PreAuthorize("hasRole('ROLE_USER')")
    public List<ProductAllViewModel> searchProducts(@RequestParam(PRODUCT_TO_LOWERCASE) String product) {

        return mapProductServiceToViewModel(productService.findByNameContaining(product));
    }

    @GetMapping("/api/category/{category}")
    @ResponseBody
    public List<ProductDetailsViewModel> getProductsByCategory(@PathVariable String category) {
        System.out.println("DEBUG: Requested category: " + category);
        List<ProductNewServiceModel> products = category.equals("All") ? 
            productService.findAll() :
            productService.findByCategoryNewId(category);
        System.out.println("DEBUG: Found " + products.size() + " products for category: " + category);
        
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDetailsViewModel.class))
                .collect(Collectors.toList());
    }
    
    @GetMapping("/api/subcategory/{subcategoryId}")
    @ResponseBody
    public List<ProductDetailsViewModel> getProductsBySubcategory(@PathVariable String subcategoryId) {
        System.out.println("DEBUG: Requested subcategory: " + subcategoryId);
        List<ProductNewServiceModel> products = productService.findBySubcategoryId(subcategoryId);
        System.out.println("DEBUG: Found " + products.size() + " products for subcategory: " + subcategoryId);
        
        return products.stream()
                .limit(10) // Limit to maximum 10 products
                .map(product -> modelMapper.map(product, ProductDetailsViewModel.class))
                .collect(Collectors.toList());
    }
    
    @GetMapping("/api/debug/categories")
    @ResponseBody
    public List<String> getDebugCategories() {
        return categoryService.findAllCategoryNames();
    }
    
    @GetMapping("/api/debug/products")
    @ResponseBody
    public List<ProductDetailsViewModel> getDebugProducts() {
        return productService.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDetailsViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/related/{productId}")
    @ResponseBody
    public List<ProductDetailsViewModel> getRelatedProducts(@PathVariable String productId) {
        try {
            // Validate productId
            if (productId == null || productId.trim().isEmpty() || "null".equals(productId) || "undefined".equals(productId)) {
                System.err.println("DEBUG: Invalid or empty productId provided: '" + productId + "'");
                return new ArrayList<>();
            }
            
            System.out.println("DEBUG: Fetching related products for productId: " + productId);
            
            // First, get the current product to find its subcategory
            ProductNewServiceModel currentProduct = productService.findById(productId);
            
            if (currentProduct == null) {
                System.err.println("DEBUG: Product not found with ID: " + productId);
                return new ArrayList<>();
            }
            
            System.out.println("DEBUG: Current product - ID: " + currentProduct.getId() + 
                             ", Name: " + currentProduct.getName() +
                             ", SubcategoryId: " + currentProduct.getSubcategoryId() + 
                             ", CategoryId: " + currentProduct.getCategoryNewId());
            
            // PRIORITY 1: Find products from the same subcategory
            if (currentProduct.getSubcategoryId() != null && !currentProduct.getSubcategoryId().trim().isEmpty()) {
                List<ProductNewServiceModel> relatedProducts = productService.findBySubcategoryId(currentProduct.getSubcategoryId());
                
                List<ProductDetailsViewModel> result = relatedProducts.stream()
                        .filter(product -> !product.getId().equals(productId)) // Exclude current product
                        .limit(12) // Show up to 12 related products
                        .map(product -> modelMapper.map(product, ProductDetailsViewModel.class))
                        .collect(Collectors.toList());
                
                System.out.println("DEBUG: Found " + result.size() + " products from same subcategory");
                
                // If we found products in the same subcategory, return them
                if (!result.isEmpty()) {
                    return result;
                }
            }
            
            // PRIORITY 2: If no subcategory products, try same category
            if (currentProduct.getCategoryNewId() != null && !currentProduct.getCategoryNewId().trim().isEmpty()) {
                List<ProductNewServiceModel> relatedProducts = productService.findByCategoryNewId(currentProduct.getCategoryNewId());
                
                List<ProductDetailsViewModel> result = relatedProducts.stream()
                        .filter(product -> !product.getId().equals(productId)) // Exclude current product
                        .limit(12) // Show up to 12 related products
                        .map(product -> modelMapper.map(product, ProductDetailsViewModel.class))
                        .collect(Collectors.toList());
                
                System.out.println("DEBUG: Found " + result.size() + " products from same category");
                return result;
            }
            
            // FALLBACK: Return random products if no category/subcategory found
            System.out.println("DEBUG: Using fallback - returning random products");
            return productService.findAll().stream()
                    .filter(product -> !product.getId().equals(productId))
                    .limit(8)
                    .map(product -> modelMapper.map(product, ProductDetailsViewModel.class))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            System.err.println("Error fetching related products for product ID " + productId + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @GetMapping("/api/product/{productId}")
    @ResponseBody
    public ProductDetailsViewModel getProductById(@PathVariable String productId) {
        try {
            System.out.println("DEBUG: API request for product ID: " + productId);
            
            ProductNewServiceModel productServiceModel = productService.findById(productId);
            if (productServiceModel == null) {
                System.out.println("DEBUG: Product not found via API: " + productId);
                return null;
            }
            
            ProductDetailsViewModel product = modelMapper.map(productServiceModel, ProductDetailsViewModel.class);
            
            // Ensure all fields are properly mapped
            if (product.getId() == null) {
                product.setId(productServiceModel.getId());
            }
            if (product.getPrice() == null && productServiceModel.getPrice() != null) {
                product.setPrice(productServiceModel.getPrice());
            }
            if (productServiceModel.getItemUnit() != null) {
                product.setItemUnit(productServiceModel.getItemUnit());
                System.out.println("DEBUG: API returning itemUnit: " + productServiceModel.getItemUnit());
            }
            
            return product;
        } catch (Exception e) {
            System.err.println("ERROR: API request failed for product " + productId + ": " + e.getMessage());
            return null;
        }
    }
    
    private ModelAndView loadAndReturnModelAndView(ProductAddBindingModel productBindingModel,
                                                   ModelAndView modelAndView) {

        List<CategoryViewModel> categories = mapCategoryServiceToViewModel(categoryService.findAll());

        modelAndView.addObject(MODEL, productBindingModel);

        modelAndView.addObject(CATEGORIES_TO_LOWER_CASE, categories);

        return view("product/add-product", modelAndView);
    }

    private List<ProductAllViewModel> mapProductServiceToViewModel(List<ProductNewServiceModel> productServiceModels){
        return productServiceModels.stream()
                .map(product -> modelMapper.map(product, ProductAllViewModel.class))
                .collect(Collectors.toList());
    }

    private List<CategoryViewModel> mapCategoryServiceToViewModel(List<CategoryNewServiceModel> categoryServiceModels){
        return categoryServiceModels.stream()
                .map(product -> modelMapper.map(product, CategoryViewModel.class))
                .collect(Collectors.toList());
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ModelAndView handleProductNotFound(ProductNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView(ERROR);
        modelAndView.addObject(MESSAGE, e.getMessage());
        modelAndView.addObject(STATUS_CODE, e.getStatusCode());

        return modelAndView;
    }

    @ExceptionHandler({ProductNameAlreadyExistsException.class})
    public ModelAndView handleProductNameALreadyExist(ProductNameAlreadyExistsException e) {
        ModelAndView modelAndView = new ModelAndView(ERROR);
        modelAndView.addObject(MESSAGE, e.getMessage());
        modelAndView.addObject(STATUS_CODE, e.getStatusCode());

        return modelAndView;
    }
}
