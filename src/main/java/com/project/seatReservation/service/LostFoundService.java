package com.project.seatReservation.service;

import com.project.seatReservation.model.FoundItem;
import com.project.seatReservation.model.LostItem;

public interface LostFoundService {
    void saveFoundItem(FoundItem foundItem);

    void saveLostItem(LostItem lostItem);
}
