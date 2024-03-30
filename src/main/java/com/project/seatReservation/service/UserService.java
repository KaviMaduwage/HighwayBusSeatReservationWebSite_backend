package com.project.seatReservation.service;

import com.project.seatReservation.model.User;

import java.util.List;

public interface UserService {
    List<User> findUsersByEmail(String email);

    User saveUser(User user);

    String confirmEmail(String confirmationToken);

    boolean isLoginSuccess(User user);
}
