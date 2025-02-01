package com.example.jobportal.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.jobportal.dao.UserDao;
import com.example.jobportal.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    BCryptPasswordEncoder encoder;

    public ResponseEntity<Object> updateUser(Integer id, MultipartFile image, String email, String username) {
        try {
            Optional<User> userData = userDao.findById(id);

            if (userData.isEmpty()) {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }

            User userExistingData = userData.get();

            // Check if the new email is already in use by another user
            if (userDao.findByEmail(email).isPresent()) {
                return new ResponseEntity<>("Email address already in use.", HttpStatus.CONFLICT);
            }

            userExistingData.setEmail(email);
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

}
