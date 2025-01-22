package com.example.jobportal.service;


import com.example.jobportal.dao.AuthDao;
import com.example.jobportal.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
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
    BCryptPasswordEncoder encoder;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<Object> signUp(String email, String username,  String password) {
        try{
            if(authDao.findByEmail(email).isPresent()){
                return new ResponseEntity<>(email + " is already registered", HttpStatus.CONFLICT);
            }

            String hashedPassword = encoder.encode(password);

            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(hashedPassword);
            authDao.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }catch (Exception e){
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

    public ResponseEntity<Object> forgotPassword(String email) {

        try {
            Optional<User> userData = authDao.findByEmail(email);

            if (userData.isEmpty()) {
                return new ResponseEntity<>("Email is not registered", HttpStatus.NOT_FOUND);
            }

            User user = userData.get();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            String htmlContent = """
        <html>
        <head>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                    background-color: #f4f4f4;
                }
                .email-container {
                    max-width: 600px;
                    margin: 20px auto;
                    background: #ffffff;
                    border: 1px solid #ddd;
                    border-radius: 8px;
                    overflow: hidden;
                }
                .email-header {
                    background-color: #007BFF;
                    color: white;
                    padding: 20px;
                    text-align: center;
                    font-size: 24px;
                    font-weight: bold;
                }
                .email-body {
                    padding: 20px;
                    color: #333333;
                    line-height: 1.6;
                }
                .email-footer {
                    background-color: #f4f4f4;
                    text-align: center;
                    padding: 10px;
                    font-size: 12px;
                    color: #888888;
                }
                .reset-button {
                    display: inline-block;
                    margin: 20px 0;
                    background-color: #007BFF;
                    color: white;
                    text-decoration: none;
                    border-radius: 5px;
                    font-weight: bold;
                    width : 120px;
                    text-align: center;
                }
                .reset-button:hover {
                    background-color: #0056b3;
                }
                .password{
                color: white;
                }
                .spam{
                color: red;
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="email-header">
                    Password Reset Request
                </div>
                <div class="email-body">
                    <p>Hello,</p>
                    <p>We received a request to reset your password. If you made this request, please click the button below to reset your password:</p>
                    <a href="https://spring.io/projects/spring-boot" class="reset-button"><p class="password">Reset Password</p></a>
                    <p class="spam">If you did not request a password reset, please ignore this email.</p>
                </div>
                <div class="email-footer">
                    &copy; 2025 JOBPortal. All rights reserved.
                </div>
            </div>
        </body>
        </html>
        """;

            // Set email properties
            helper.setTo(email);
            helper.setSubject("Forgot Password");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            return new ResponseEntity<>("Password reset email sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during the process: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
