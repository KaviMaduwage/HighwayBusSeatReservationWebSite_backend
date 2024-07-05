package com.project.seatReservation.dao;

import com.project.seatReservation.model.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LostItemDao extends JpaRepository<LostItem,Integer> {


    @Query("SELECT l FROM LostItem  l ORDER BY l.incidentDate DESC")
    List<LostItem> loadLostItems();

    @Query("SELECT l FROM LostItem l WHERE l.incidentDate >= DATE( :searchDate)")
    List<LostItem> getLostItemsAfterDate(String searchDate);

    @Query("SELECT l FROM LostItem l WHERE l.user.userId = :userId ORDER BY l.incidentDate DESC")
    List<LostItem> getLostItemsByUserId(int userId);

    @Query("SELECT l FROM LostItem  l WHERE l.lostItemId = :lostItemId")
    List<LostItem> findLostItemById(int lostItemId);
}
