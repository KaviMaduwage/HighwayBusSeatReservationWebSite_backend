package com.project.seatReservation.dao;

import com.project.seatReservation.model.BusOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusOwnerDao extends JpaRepository<BusOwner, Integer> {
    @Query("SELECT b FROM BusOwner b WHERE b.user.userId = :busOwnerUserId")
    List<BusOwner> findBusOwnerByUserId(int busOwnerUserId);

    @Query("SELECT b FROM BusOwner b WHERE b.user.isActive = true")
    List<BusOwner> findActiveBusOwnerList();
}
