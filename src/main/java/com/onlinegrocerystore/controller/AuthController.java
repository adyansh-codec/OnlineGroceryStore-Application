package com.onlinegrocerystore.controller;

import org.softuni.onlinegrocery.domain.entities.User;
import com.onlinegrocerystore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request, HttpSession session) {
        try {
            String usernameOrEmail = request.get("username");
            String password = request.get("password");

            User user = userService.authenticate(usernameOrEmail, password);
            
            if (user != null) {
                // Store user in session
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("email", user.getEmail());
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("userId", user.getId());
                response.put("username", user.getUsername());
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

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request, HttpSession session) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            String address = request.get("address");

            User user = userService.createUser(username, email, password, address);
            
            // Auto-login after registration
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("email", user.getEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
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

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        Object userId = session.getAttribute("userId");
        Object username = session.getAttribute("username");
        Object email = session.getAttribute("email");
        
        if (userId != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("loggedIn", true);
            response.put("userId", userId);
            response.put("username", username);
            response.put("email", email);
            return ResponseEntity.ok(response);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("loggedIn", false);
        return ResponseEntity.ok(response);
    }
}