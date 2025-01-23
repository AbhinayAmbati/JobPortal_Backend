package com.example.jobportal.controllers;

import com.example.jobportal.models.User;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("profile")
    public ResponseEntity<Object> getUserProfile(@RequestParam Integer id) {
        return userService.getUserProfile(
                id
        );
    }

    @PutMapping("update/profile")
    public ResponseEntity<Object> updateUserProfileImage(@RequestParam Integer id,@RequestParam("image") MultipartFile image) throws IOException {
        return userService.updateUserProfileImage(
                id,
                image
        );
    }

    @PutMapping("update")
    public ResponseEntity<Object> updateUser(@RequestBody User user){
        return userService.updateUser(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getProfileimage()
        );
    }

}
