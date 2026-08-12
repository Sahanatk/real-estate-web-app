package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.service.passwordResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class loginController {
    private final passwordResetService pwdResetServ;

    public loginController(passwordResetService pwdResetServ) {
        this.pwdResetServ = pwdResetServ;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email,
                                       RedirectAttributes redirectAttributes) {
        // For now - just show a success message
        // Later you can integrate email sending (e.g. JavaMailSender)
        redirectAttributes.addFlashAttribute("message",
                "If an account exists for " + email + ", a reset link has been sent.");
        return "redirect:/forgot-password?sent";
    }
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        String status = pwdResetServ.validateToken(token);
        if (!status.equals("valid")) {
            model.addAttribute("error", status.equals("expired")
                    ? "This reset link has expired. Please request a new one."
                    : "Invalid reset link.");
            return "forgot-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String token,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?token=" + token;
        }
        if (password.length() < 8) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters.");
            return "redirect:/reset-password?token=" + token;
        }

        boolean success = pwdResetServ.resetPassword(token, password);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Password reset successfully. Please log in.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Reset link is invalid or expired.");
            return "redirect:/forgot-password";
        }
    }

}
