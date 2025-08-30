package com.onlinegrocerystore.service;

import org.softuni.onlinegrocery.domain.entities.User;
import com.onlinegrocerystore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String usernameOrEmail, String password) {
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Simple password check - in production, use proper password hashing
            if (password.equals(user.getPassword()) && user.isEnabled()) {
                return user;
            }
        }
        return null;
    }

    public User createUser(String username, String email, String password, String address) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setAddress(address);
        return userRepository.save(user);
    }

	/*
	 * public Optional<User> findByUsername(String username) { return
	 * userRepository.findByUsername(username); }
	 * 
	 * public Optional<User> findById(Long id) { return userRepository.findById(id);
	 * }
	 */

    public User saveUser(User user) {
        return userRepository.save(user);
    }

	public Object findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
}