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

    @PutMapping("user-info")
    public ResponseEntity<Object> updateUserProfileInfo(@RequestParam Integer id,@RequestBody User user){

        return userService.updateUserProfileInfo(
                id,
                user.getCurrentPosition(),
                user.getEducation(),
                user.getLocation(),
                user.getPhone(),
                user.getGitHubUrl(),
                user.getLinkedInUrl(),
                user.getPortfolioUrl()
        );
    }

    @PutMapping("update")
    public ResponseEntity<Object> updateUser(
            @RequestParam Integer id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam String username,
            @RequestParam String currentPosition,
            @RequestParam String location,
            @RequestParam String education,
            @RequestParam String phone,
            @RequestParam String portfolio,
            @RequestParam String github,
            @RequestParam String linkedin
    ) {
        return userService.updateUser(id, image, username,currentPosition,location,education,phone,portfolio,github,linkedin);
    }

}
