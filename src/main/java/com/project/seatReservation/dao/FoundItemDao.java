package com.project.seatReservation.dao;

import com.project.seatReservation.model.FoundItem;
import com.project.seatReservation.model.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoundItemDao extends JpaRepository<FoundItem, Integer> {

    @Query("SELECT f FROM FoundItem f ORDER BY f.reportedDate DESC")
    List<FoundItem> loadFoundItems();

    @Query("SELECT f FROM FoundItem f WHERE f.reportedDate >= DATE( :searchDate)")
    List<FoundItem> getFoundItemsAfterDate(String searchDate);

    @Query("SELECT f FROM FoundItem f WHERE f.user.userId = :userId ORDER BY f.reportedDate DESC")
    List<FoundItem> foundItemList(int userId);

    @Query("SELECT f FROM FoundItem f WHERE f.foundItemId = :foundItemId")
    List<FoundItem> findFoundItemById(int foundItemId);
}
