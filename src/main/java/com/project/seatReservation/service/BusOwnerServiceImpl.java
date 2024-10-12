package com.project.seatReservation.service;

import com.project.seatReservation.dao.BusOwnerDao;
import com.project.seatReservation.model.BusOwner;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BusOwnerServiceImpl implements BusOwnerService{
    BusOwnerDao busOwnerDao;

    public BusOwnerServiceImpl(BusOwnerDao busOwnerDao) {
        this.busOwnerDao = busOwnerDao;
    }

    @Override
    public void saveBusOwner(BusOwner busOwner) {
        busOwnerDao.save(busOwner);
    }

    @Override
    public List<BusOwner> findBusOwnerByUserId(int busOwnerUserId) {
        return busOwnerDao.findBusOwnerByUserId(busOwnerUserId);
    }

    @Override
    public List<BusOwner> findActiveBusOwnerList() {
        return busOwnerDao.findActiveBusOwnerList();
    }

    @Override
    public List<BusOwner> getAllBusOwners() {
        return busOwnerDao.getAllBusOwners();
    }

    @Override
    @Transactional
    public void updateBusOwner(BusOwner busOwner) {
        busOwnerDao.save(busOwner);
    }
}
