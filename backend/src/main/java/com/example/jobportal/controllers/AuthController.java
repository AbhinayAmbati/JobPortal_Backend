package com.example.jobportal.controllers;


import com.example.jobportal.models.User;
import com.example.jobportal.service.AuthService;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/api/user")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    JavaMailSender mailSender;

    @PostMapping("signup")
    public ResponseEntity<Object> signUp(@RequestBody User user) {
        return authService.signUp(
                user.getEmail(),
                user.getUsername(),
                user.getPassword()
        );
    }

    @PostMapping("signin")
    public ResponseEntity<Object> signIn(@RequestBody User user) {
        return authService.signIn(
                user.getEmail(),
                user.getPassword()
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            String resetToken = userService.createPasswordResetTokenForUser(email);
            sendPasswordResetEmail(email, resetToken);
            return ResponseEntity.ok().body(Map.of("message", "Reset link sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    private void sendPasswordResetEmail(String email, String token) {
        String resetUrl = "http://localhost:5173" + "/reset-password/" + token;
        String subject = "Password Reset Request";
        String content = String.format(
                "Please click on the link below to reset your password:\n%s\n\nThe link will expire in 30 minutes.",
                resetUrl
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @GetMapping("/verify-reset-token/{token}")
    public ResponseEntity<?> verifyResetToken(@PathVariable String token) {

        boolean isValid = userService.validatePasswordResetToken(token);
        if (isValid) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired token"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok().body(Map.of("message", "Password reset successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
