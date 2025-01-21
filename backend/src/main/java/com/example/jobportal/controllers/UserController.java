package com.example.jobportal.controllers;

import com.example.jobportal.models.User;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("update")
    public Object updateUser(@RequestBody User user){
        return userService.updateUser(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword()
        );
    }

}
