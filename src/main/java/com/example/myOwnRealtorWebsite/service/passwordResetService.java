package com.example.myOwnRealtorWebsite.service;

import com.example.myOwnRealtorWebsite.model.PasswordResetToken;
import com.example.myOwnRealtorWebsite.model.User;
import com.example.myOwnRealtorWebsite.repository.passwordResetTokenRepository;
import com.example.myOwnRealtorWebsite.repository.userRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class passwordResetService {

    private final userRepository userRepo;
    private final passwordResetTokenRepository tokenRepo;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public passwordResetService(userRepository userRepo,
                                passwordResetTokenRepository tokenRepo,
                                JavaMailSender mailSender,
                                PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void sendResetEmail(String email) {
        Optional<User> userOpt = userRepo.findByEmail(email);

        // Always show success even if email not found (security best practice)
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();

        // Delete any existing token for this user
        tokenRepo.deleteByUser_Id(user.getId());

        // Generate new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepo.save(resetToken);

        // Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request - MyOwnRealtor");
        message.setText(
                "Hi " + user.getFullName() + ",\n\n" +
                        "You requested a password reset. Click the link below to reset your password:\n\n" +
                        "http://localhost:8080/reset-password?token=" + token + "\n\n" +
                        "This link expires in 30 minutes.\n\n" +
                        "If you didn't request this, please ignore this email.\n\n" +
                        "Best regards,\nSindhu Jakka - MyOwnRealtor"
        );
        mailSender.send(message);
    }

    @Transactional
    public String validateToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepo.findByToken(token);
        if (tokenOpt.isEmpty()) return "invalid";
        if (tokenOpt.get().isExpired()) return "expired";
        return "valid";
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepo.findByToken(token);
        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) return false;

        User user = tokenOpt.get().getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        // Delete used token
        tokenRepo.delete(tokenOpt.get());
        return true;
    }
}
