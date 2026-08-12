package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.model.User;
import com.example.myOwnRealtorWebsite.service.userService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class registerController {
    private final userService userServ;

    public registerController(userService userServ) {
        this.userServ = userServ;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles",
                Arrays.stream(User.Role.values())
                        .filter(r -> r != User.Role.ADMIN)
                        .toList()
        );
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userServ.registerUser(user);
            model.addAttribute("message","Registration successful.Please Login");
            return "redirect:/login";
        }
        catch (Exception e) {
            model.addAttribute("error", "An error occurred during registration.Please try again later");
            return "register";
        }
    }
}
