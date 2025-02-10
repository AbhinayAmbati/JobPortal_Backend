package com.example.jobportal.service;


import com.cloudinary.Cloudinary;
import com.example.jobportal.dao.AuthDao;
import com.example.jobportal.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

@Configuration
@Service
public class AuthService {


    @Autowired
    AuthDao authDao;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<Object> signUp(String email, String username, String password) {
        try {
            if (authDao.findByEmail(email).isPresent()) {
                return new ResponseEntity<>(email + " is already registered.", HttpStatus.CONFLICT);
            }

            String hashedPassword = encoder.encode(password);

            // Create a new user and save it to the database
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(hashedPassword);
            authDao.save(user);

            return new ResponseEntity<>(user, HttpStatus.CREATED);

        } catch (Exception e) {
            // Handle errors gracefully
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    public ResponseEntity<Object> signIn(String email, String password) {
        try {
            Optional<User> userData = authDao.findByEmail(email);

            if (userData.isEmpty()) {
                return new ResponseEntity<>("Email is not registered", HttpStatus.NOT_FOUND);
            }

            User user = userData.get();

            if (!passwordEncoder.matches(password, user.getPassword())) {
                return new ResponseEntity<>("Incorrect password.", HttpStatus.UNAUTHORIZED);
            }

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = keyGen.generateKeyPair();

            String jwtToken = Jwts.builder().setSubject(user.getUsername()).setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis()+3600000))
                    .signWith(SignatureAlgorithm.RS256,keyPair.getPrivate())
                    .compact();

            class JwtResponse{
                private String jwtToken;
                private User user;

                public JwtResponse(String jwtToken, User user) {
                    this.jwtToken = jwtToken;
                    this.user = user;
                }
                public String getJwtToken() {
                    return jwtToken;
                }
                public User getUser() {
                    return user;
                }
                public void setUser(User user) {
                    this.user = user;
                }
                public void setJwtToken(String jwtToken) {
                    this.jwtToken = jwtToken;
                }
                public String toString(String jwtToken, User user){
                    return jwtToken + " " + user.getUsername();
                }
            }

            JwtResponse  jwtResponse = new JwtResponse(jwtToken,user);

            return ResponseEntity.ok(jwtResponse);

        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during sign-in: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendPasswordResetEmail(String email, String token) {
        String resetUrl = "http://localhost:5173" + "/reset-password/" + token;
        String subject = "Password Reset Request";
        String content = String.format("""
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Password Reset</title>
        <style>
            body {
                font-family: 'Segoe UI', Arial, sans-serif;
                line-height: 1.7;
                color: #2d3748;
                margin: 0;
                padding: 0;
                background-color: #f7fafc;
            }
            .container {
                max-width: 600px;
                margin: 40px auto;
                padding: 0;
                background-color: #ffffff;
                border-radius: 12px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            .header {
                background: linear-gradient(135deg, #4299e1 0%%, #3182ce 100%%);
                padding: 32px 20px;
                text-align: center;
                border-radius: 12px 12px 0 0;
            }
            .header h1 {
                color: #ffffff;
                margin: 0;
                font-size: 28px;
                font-weight: 600;
                letter-spacing: 0.5px;
            }
            .content {
                padding: 40px 32px;
                background-color: #ffffff;
                border-radius: 0 0 12px 12px;
            }
            .button {
                display: inline-block;
                padding: 14px 32px;
                background: linear-gradient(135deg, #4299e1 0%%, #3182ce 100%%);
                color: #ffffff;
                text-decoration: none;
                border-radius: 8px;
                margin: 24px 0;
                font-weight: 600;
                letter-spacing: 0.5px;
                transition: transform 0.2s ease, box-shadow 0.2s ease;
                box-shadow: 0 2px 4px rgba(66, 153, 225, 0.3);
            }
            .button:hover {
                transform: translateY(-1px);
                box-shadow: 0 4px 8px rgba(66, 153, 225, 0.4);
            }
            .note {
                font-size: 14px;
                color: #718096;
                margin-top: 24px;
                padding: 16px;
                background-color: #f8fafc;
                border-radius: 8px;
                border-left: 4px solid #4299e1;
            }
            .footer {
                text-align: center;
                margin-top: 32px;
                padding-top: 24px;
                border-top: 1px solid #e2e8f0;
                font-size: 13px;
                color: #718096;
            }
            p {
                margin: 16px 0;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>Password Reset Request</h1>
            </div>
            <div class="content">
                <p>Hello,</p>
                <p>We received a request to reset your password. To create a new password, please click the secure button below:</p>
                <div style="text-align: center;">
                    <a href="%s" class="button">Reset Password</a>
                </div>
                <div class="note">
                    <strong>Security Notice:</strong>
                    <p style="margin: 8px 0 0 0">• This link will expire in 30 minutes</p>
                    <p style="margin: 4px 0 0 0">• If you didn't request this reset, please ignore this email</p>
                    <p style="margin: 4px 0 0 0">• Contact our support team if you have any concerns</p>
                </div>
            </div>
            <div class="footer">
                <p>This is an automated message. Please do not reply to this email.</p>
                <p style="margin-top: 8px;">© 2025 Job Portal. All rights reserved.</p>
            </div>
        </div>
    </body>
    </html>
""", resetUrl);

        // Create MimeMessage instead of SimpleMailMessage to support HTML
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true); // Set second parameter to true for HTML
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    public ResponseEntity<Object> jwtToken(String email) throws NoSuchAlgorithmException {

        try{
            Optional<User> userData = authDao.findByEmail(email);

            if (userData.isEmpty()) {
                return new ResponseEntity<>("Invalid email", HttpStatus.UNAUTHORIZED);
            }

            User user = userData.get();

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = keyGen.generateKeyPair();

            String jwtToken = Jwts.builder().setSubject(user.getUsername()).setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                    .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                    .compact();

            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
