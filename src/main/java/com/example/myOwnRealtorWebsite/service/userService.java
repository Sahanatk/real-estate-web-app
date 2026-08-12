package com.example.myOwnRealtorWebsite.service;

import com.example.myOwnRealtorWebsite.model.User;
import com.example.myOwnRealtorWebsite.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.lang.*;

@Service
public class userService {
    private final userRepository userRep;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public userService(userRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRep = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User getByUserId(Long id) {
        return userRep.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User saveUser(User user) {
        return userRep.save(user);
    }

    public void registerUser(User user) {

        if (userRep.existsByFullName(user.getFullName())) {

            throw new RuntimeException("user already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRep.save(user);
    }

    public User getByEmail(String email) {
        return userRep.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
