package com.example.jobportal.dao;


import com.example.jobportal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping
public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
