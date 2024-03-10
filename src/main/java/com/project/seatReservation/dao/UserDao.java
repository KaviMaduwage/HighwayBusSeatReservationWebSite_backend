package com.project.seatReservation.dao;

import com.project.seatReservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    List<User> findUsersByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.verificationCode = :confirmationToken")
    User findByConfirmationToken(String confirmationToken);
}
