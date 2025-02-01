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

    @PutMapping("update")
    public ResponseEntity<Object> updateUser(
            @RequestParam Integer id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam String email,
            @RequestParam String username
    ) {
        return userService.updateUser(id, image, email, username);
    }

}
