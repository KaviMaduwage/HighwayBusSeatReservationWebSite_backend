package com.project.seatReservation.service;

import com.project.seatReservation.dao.FoundItemDao;
import com.project.seatReservation.dao.LostItemDao;
import com.project.seatReservation.model.FoundItem;
import com.project.seatReservation.model.LostItem;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<LostItem> loadLostItems() {
        return lostItemDao.loadLostItems();
    }

    @Override
    public List<LostItem> getLostItemsAfterDate(String searchDate) {
        return lostItemDao.getLostItemsAfterDate(searchDate);
    }

    @Override
    public List<FoundItem> loadFoundItems() {
        return foundItemDao.loadFoundItems();
    }

    @Override
    public List<FoundItem> getFoundItemsAfterDate(String searchDate) {
        return foundItemDao.getFoundItemsAfterDate(searchDate);
    }

    @Override
    public List<LostItem> getLostItemsByUserId(int userId) {
        return lostItemDao.getLostItemsByUserId(userId);
    }

    @Override
    public List<FoundItem> getFoundItemsByUserId(int userId) {
        return foundItemDao.foundItemList(userId);
    }

    @Override
    public List<FoundItem> findFoundItemById(int foundItemId) {
        return foundItemDao.findFoundItemById(foundItemId);
    }

    @Override
    public void deleteFoundItemById(FoundItem foundItem) {
        foundItemDao.delete(foundItem);
    }

    @Override
    public List<LostItem> findLostItemById(int lostItemId) {
        return lostItemDao.findLostItemById(lostItemId);
    }

    @Override
    public void deleteLostItemById(LostItem lostItem) {
        lostItemDao.delete(lostItem);
    }
}
