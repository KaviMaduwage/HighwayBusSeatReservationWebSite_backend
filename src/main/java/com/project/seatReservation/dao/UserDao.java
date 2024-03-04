package com.project.seatReservation.dao;

import com.project.seatReservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
}
