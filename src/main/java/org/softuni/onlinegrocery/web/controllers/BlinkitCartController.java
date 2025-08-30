package org.softuni.onlinegrocery.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.onlinegrocery.domain.models.service.ProductNewServiceModel;
import org.softuni.onlinegrocery.domain.models.view.ProductDetailsViewModel;
import org.softuni.onlinegrocery.service.ProductService;
import org.softuni.onlinegrocery.service.UserService;
import org.softuni.onlinegrocery.web.annotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/cart")
public class BlinkitCartController extends BaseController {

    private final ProductService productService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public BlinkitCartController(ProductService productService, UserService userService, ModelMapper modelMapper) {
        this.productService = productService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    // Cart item model for session storage
    public static class CartItem {
        private String id;
        private String name;
        private BigDecimal price;
        private String imageUrl;
        private String unit;
        private int quantity;
        private long addedAt;

        // Constructors
        public CartItem() {}

        public CartItem(String id, String name, BigDecimal price, String imageUrl, String unit, int quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.unit = unit;
            this.quantity = quantity;
            this.addedAt = System.currentTimeMillis();
        }

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public long getAddedAt() { return addedAt; }
        public void setAddedAt(long addedAt) { this.addedAt = addedAt; }
        
        public BigDecimal getTotalPrice() {
            return price.multiply(new BigDecimal(quantity));
        }
    }

    @PostMapping("/add-product")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addProduct(@RequestParam String id, 
                                                         @RequestParam(defaultValue = "1") int quantity,
                                                         HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Get product details
            ProductNewServiceModel product = productService.findById(id);
            if (product == null) {
                response.put("success", false);
                response.put("message", "Product not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Get cart from session
            Map<String, CartItem> cart = getCartFromSession(session);
            
            // Add or update item
            if (cart.containsKey(id)) {
                CartItem existingItem = cart.get(id);
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                CartItem newItem = new CartItem(
                    id,
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl(),
                    product.getUnit() != null ? product.getUnit() : "1 unit",
                    quantity
                );
                cart.put(id, newItem);
            }
            
            // Save cart to session
            session.setAttribute("BLINKIT_CART", cart);
            
            response.put("success", true);
            response.put("message", "Product added to cart");
            response.put("cartInfo", getCartSummary(cart));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding product to cart");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/remove-product")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeProduct(@RequestParam String id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, CartItem> cart = getCartFromSession(session);
            
            if (cart.containsKey(id)) {
                CartItem item = cart.get(id);
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    cart.remove(id);
                }
            }
            
            session.setAttribute("BLINKIT_CART", cart);
            
            response.put("success", true);
            response.put("message", "Product updated in cart");
            response.put("cartInfo", getCartSummary(cart));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error removing product from cart");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/api/items")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartItems(HttpSession session) {
        Map<String, CartItem> cart = getCartFromSession(session);
        Map<String, Object> response = new HashMap<>();
        
        response.put("items", cart.values());
        response.put("summary", getCartSummary(cart));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Cart Details")
    public ModelAndView cartDetails(ModelAndView modelAndView, HttpSession session) {
        Map<String, CartItem> cart = getCartFromSession(session);
        Map<String, Object> summary = getCartSummary(cart);
        
        modelAndView.addObject("cartItems", cart.values());
        modelAndView.addObject("totalItems", summary.get("totalItems"));
        modelAndView.addObject("totalPrice", summary.get("totalPrice"));
        
        return view("cart/blinkit-cart-details", modelAndView);
    }

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView checkout(HttpSession session, Principal principal) {
        Map<String, CartItem> cart = getCartFromSession(session);
        
        if (cart.isEmpty()) {
            return redirect("/home");
        }
        
        // Process checkout logic here
        // For now, just clear the cart and redirect
        session.removeAttribute("BLINKIT_CART");
        
        return redirect("/home?checkout=success");
    }

    @PostMapping("/clear")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearCart(HttpSession session) {
        session.removeAttribute("BLINKIT_CART");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Cart cleared");
        
        return ResponseEntity.ok(response);
    }

    @SuppressWarnings("unchecked")
    private Map<String, CartItem> getCartFromSession(HttpSession session) {
        Object cartObj = session.getAttribute("BLINKIT_CART");
        if (cartObj instanceof Map) {
            return (Map<String, CartItem>) cartObj;
        }
        return new HashMap<>();
    }

    private Map<String, Object> getCartSummary(Map<String, CartItem> cart) {
        Map<String, Object> summary = new HashMap<>();
        
        int totalItems = cart.values().stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalPrice = cart.values().stream()
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        summary.put("totalItems", totalItems);
        summary.put("totalPrice", totalPrice);
        summary.put("itemCount", cart.size());
        
        return summary;
    }
}
