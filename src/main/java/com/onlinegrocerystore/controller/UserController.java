package com.onlinegrocerystore.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

import org.softuni.onlinegrocery.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlinegrocerystore.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request, 
                                                    HttpSession session) {
        try {
            String usernameOrEmail = request.get("username");
            String password = request.get("password");

            if (usernameOrEmail == null || password == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Username and password are required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            User user = userService.authenticate(usernameOrEmail, password);
            
            if (user != null) {
                // Set user in session
                session.setAttribute("loggedInUser", user.getUsername());
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("userId", user.getUsername());
                response.put("email", user.getEmail());
                response.put("message", "Login successful");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Invalid credentials");
                return ResponseEntity.ok(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }

    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request, 
                                                       HttpSession session) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            String address = request.get("address");

            if (username == null || email == null || password == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "All fields are required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            User user = userService.createUser(username, email, password, address);
            
            // Auto-login after registration
            session.setAttribute("loggedInUser", user.getUsername());
            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", user.getUsername());
            response.put("email", user.getEmail());
            response.put("message", "Registration successful");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }

    @PostMapping("/api/auth/logout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/auth/current-user")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        Object user = session.getAttribute("loggedInUser");
        Object fullName = session.getAttribute("userFullName");
        
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("loggedIn", true);
            response.put("userId", user.toString());
            response.put("fullName", fullName != null ? fullName.toString() : user.toString());
            return ResponseEntity.ok(response);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("loggedIn", false);
        return ResponseEntity.ok(response);
    }

    
}