package com.example.jobportal.dao;

import com.example.jobportal.models.PasswordReset;
import com.example.jobportal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetDao extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByToken(String token);

    void deleteByUser(User user);
}
