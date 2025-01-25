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

    public ResponseEntity<Object> updateUser(Integer id,String email, String username, String password, String profileimage) {

        try {
            Optional<User> userData = userDao.findById(id);

            if (userData.isEmpty()) {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }

            User userExistingData;

            if (email.equals(userData.get().getEmail())) {
                return new ResponseEntity<>("Email address already in use.", HttpStatus.CONFLICT);
            }

            userExistingData = userData.get();


            userExistingData.setEmail(email);
            if (username == null) {
                userExistingData.setUsername(userData.get().getUsername());
            } else {
                userExistingData.setUsername(username);
            }
            if (password == null) {
                userExistingData.setPassword(userData.get().getPassword());
            } else {
                userExistingData.setPassword(encoder.encode(password));
            }
            if(profileimage == null){
                userExistingData.setProfileimage(userData.get().getProfileimage());
            }else{
                userExistingData.setProfileimage(profileimage);
            }
        userDao.save(userExistingData);

        return new ResponseEntity<>("User Updated Successfully.", HttpStatus.OK);
    }catch (Exception e){
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

    public ResponseEntity<Object> updateUserProfileImage(Integer id, MultipartFile image) throws IOException {
        Optional<User> userData = userDao.findById(id);
        if (userData.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        User userExistingData;

        Map uploadImage = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = uploadImage.get("url").toString();

        userExistingData = userData.get();
        userExistingData.setProfileimage(imageUrl);

        userDao.save(userExistingData);

        return new ResponseEntity<>("User Updated Successfully.", HttpStatus.OK);

    }
}
