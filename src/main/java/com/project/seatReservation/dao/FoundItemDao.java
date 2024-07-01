package com.project.seatReservation.dao;

import com.project.seatReservation.model.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoundItemDao extends JpaRepository<FoundItem, Integer> {
}
