package com.project.seatReservation.service;

import com.project.seatReservation.model.FoundItem;
import com.project.seatReservation.model.LostItem;

import java.util.List;

public interface LostFoundService {
    void saveFoundItem(FoundItem foundItem);

    void saveLostItem(LostItem lostItem);

    List<LostItem> loadLostItems();

    List<LostItem> getLostItemsAfterDate(String searchDate);

    List<FoundItem> loadFoundItems();

    List<FoundItem> getFoundItemsAfterDate(String searchDate);

    List<LostItem> getLostItemsByUserId(int userId);

    List<FoundItem> getFoundItemsByUserId(int userId);

    List<FoundItem> findFoundItemById(int foundItemId);

    void deleteFoundItemById(FoundItem foundItemId);

    List<LostItem> findLostItemById(int lostItemId);

    void deleteLostItemById(LostItem lostItem);
}
