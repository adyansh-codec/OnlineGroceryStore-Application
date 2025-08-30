package org.softuni.onlinegrocery.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.softuni.onlinegrocery.domain.models.service.ProductNewServiceModel;
import org.softuni.onlinegrocery.domain.models.view.ProductDetailsViewModel;
import org.softuni.onlinegrocery.service.ProductService;

@Controller
@RequestMapping("/productdesc")
public class ProductDescController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductDescController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    // Redirect all productdesc requests to /products
    @GetMapping("/{productId}")
    public String redirectToProducts(@PathVariable("productId") String productId) {
        System.out.println("=== ProductDescController: Redirecting /productdesc/" + productId + " to /products/" + productId + " ===");
        return "redirect:/products/" + productId;
    }
    
    // Also handle the test endpoint with redirect
    @GetMapping("/test")
    public String redirectTestToProducts() {
        System.out.println("=== ProductDescController: Redirecting /productdesc/test to /products ===");
        return "redirect:/products";
    }
}