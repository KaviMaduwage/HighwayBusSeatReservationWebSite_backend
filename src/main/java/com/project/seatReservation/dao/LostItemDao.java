package com.project.seatReservation.dao;

import com.project.seatReservation.model.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostItemDao extends JpaRepository<LostItem,Integer> {
}
