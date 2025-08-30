package org.softuni.onlinegrocery.web.controllers;

import org.softuni.onlinegrocery.domain.entities.User;
import org.softuni.onlinegrocery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthRestController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthRestController(UserService userService, 
                             PasswordEncoder passwordEncoder,
                             AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData, 
                                                   HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");

            if (email == null || password == null) {
                response.put("success", false);
                response.put("message", "Email and password are required");
                return ResponseEntity.ok(response);
            }

            // Find user by email
            User user = userService.findByEmail(email);
            
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }

            // Authenticate using username (as Spring Security expects)
            try {
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), password)
                );
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // Update session
                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("username", user.getUsername());
                response.put("redirectUrl", "/home");
                
            } catch (BadCredentialsException e) {
                response.put("success", false);
                response.put("message", "Invalid credentials");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            SecurityContextHolder.clearContext();
            
            // Invalidate session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            
            response.put("success", true);
            response.put("message", "Logged out successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Logout failed");
            return ResponseEntity.ok(response);
        }
    }
}
