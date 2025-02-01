package com.example.jobportal.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.jobportal.dao.PasswordResetDao;
import com.example.jobportal.dao.UserDao;
import com.example.jobportal.models.PasswordReset;
import com.example.jobportal.models.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetDao passwordResetDao;

    public ResponseEntity<Object> updateUser(Integer id, MultipartFile image,String username,String currentPosition,
                                              String location,
                                              String education,
                                              String phone,
                                              String portfolio,
                                              String github,
                                              String linkedin ) {
        try {
            Optional<User> userData = userDao.findById(id);

            if (userData.isEmpty()) {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }

            User userExistingData = userData.get();


            userExistingData.setUsername(username);

            if (image != null && !image.isEmpty()) {
                // Delete old image if exists
                String oldImageUrl = userExistingData.getProfileimage();
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    try {
                        String publicId = oldImageUrl.substring(oldImageUrl.lastIndexOf("/") + 1, oldImageUrl.lastIndexOf("."));
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    } catch (Exception e) {
                        // Log error but continue with upload of new image
                        return new ResponseEntity<>("Error deleting old image from Cloudinary: " + e.getMessage(),HttpStatus.LOOP_DETECTED);
                    }
                }

                // Upload new image
                Map uploadImage = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = uploadImage.get("url").toString();
                userExistingData.setProfileimage(imageUrl);
            }

            userExistingData.setCurrentPosition(currentPosition);
            userExistingData.setLocation(location);
            userExistingData.setEducation(education);
            userExistingData.setPhone(phone);
            userExistingData.setGitHubUrl(github);
            userExistingData.setLinkedInUrl(linkedin);
            userExistingData.setPortfolioUrl(portfolio);

            userDao.save(userExistingData);
            return new ResponseEntity<>("User Updated Successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Object> getUserProfile(@RequestParam Integer id) {
        try{
            Optional<User> userData = userDao.findById(id);
            if (userData.isEmpty()) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String createPasswordResetTokenForUser(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete any existing tokens for this user
        passwordResetDao.deleteByUser(user);

        // Create new token
        String token = UUID.randomUUID().toString();
        PasswordReset resetToken = new PasswordReset();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        passwordResetDao.save(resetToken);

        return token;
    }

    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordReset> resetToken = passwordResetDao.findByToken(token);
        if (resetToken.isEmpty() || resetToken.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    public void resetPassword(String token, String newPassword) {
        PasswordReset resetToken = passwordResetDao.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = resetToken.getUser();

        if (user == null) {
            throw new RuntimeException("No user associated with this reset token.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
        passwordResetDao.delete(resetToken);
    }

    public ResponseEntity<Object> updateUserProfileInfo(Integer id,String currentPosition, String education, String location, String phone, String gitHubUrl, String linkedInUrl, String portfolioUrl) {
        try{

            Optional<User> userData = userDao.findById(id);
            if (userData.isEmpty()) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            User user = userData.get();

            user.setCurrentPosition(currentPosition);
            user.setEducation(education);
            user.setLocation(location);
            user.setPhone(phone);
            user.setGitHubUrl(gitHubUrl);
            user.setLinkedInUrl(linkedInUrl);
            user.setPortfolioUrl(portfolioUrl);
            userDao.save(user);
            return new ResponseEntity<>("User Updated Successfully.", HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
