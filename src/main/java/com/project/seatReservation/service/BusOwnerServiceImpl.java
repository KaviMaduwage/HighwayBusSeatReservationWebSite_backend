package com.project.seatReservation.service;

import com.project.seatReservation.dao.BusOwnerDao;
import com.project.seatReservation.model.BusOwner;
import org.springframework.stereotype.Service;

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
}
