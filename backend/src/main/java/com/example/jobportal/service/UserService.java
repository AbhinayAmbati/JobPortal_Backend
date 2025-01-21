package com.example.jobportal.service;

import com.example.jobportal.dao.UserDao;
import com.example.jobportal.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    BCryptPasswordEncoder encoder;

    public ResponseEntity<Object> updateUser(Integer id,String email, String username, String password) {
        try{
            Optional<User> userData = userDao.findById(id);

            if (userData.isEmpty()) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            if (email.equals(userData.get().getEmail())) {
                return new ResponseEntity<>("Email address already in use", HttpStatus.CONFLICT);
            }

            User userExistingData = userData.get();

            String hashedPassword = encoder.encode(password);

            userExistingData.setEmail(email);
            userExistingData.setUsername(username);
            userExistingData.setPassword(hashedPassword);
            userDao.save(userExistingData);

            return new ResponseEntity<>("User Updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
