package com.project.seatReservation.service;

import com.project.seatReservation.dao.FoundItemDao;
import com.project.seatReservation.dao.LostItemDao;
import com.project.seatReservation.model.FoundItem;
import com.project.seatReservation.model.LostItem;
import org.springframework.stereotype.Service;

@Service
public class LostFoundServiceImpl implements LostFoundService{

    LostItemDao lostItemDao;

    FoundItemDao foundItemDao;

    public LostFoundServiceImpl(LostItemDao lostItemDao, FoundItemDao foundItemDao) {
        this.lostItemDao = lostItemDao;
        this.foundItemDao = foundItemDao;
    }

    @Override
    public void saveFoundItem(FoundItem foundItem) {
        foundItemDao.save(foundItem);
    }

    @Override
    public void saveLostItem(LostItem lostItem) {
        lostItemDao.save(lostItem);
    }
}
