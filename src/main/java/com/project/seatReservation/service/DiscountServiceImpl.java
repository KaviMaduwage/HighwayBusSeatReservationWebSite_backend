package com.project.seatReservation.service;

import com.project.seatReservation.dao.DiscountDao;
import com.project.seatReservation.model.Discount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService{
    DiscountDao discountDao;

    public DiscountServiceImpl(DiscountDao discountDao) {
        this.discountDao = discountDao;
    }

    @Override
    public void saveDiscount(Discount discount) {
        discountDao.save(discount);
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountDao.findAll();
    }

    @Override
    public Discount getDiscountById(int discountId) {
        return discountDao.getDiscountById(discountId);
    }

    @Override
    public List<Discount> findDiscountsByDateRouteAndBusOwner(String discountDate, int routeId, int busOwnerId) {
        return discountDao.findDiscountsByDateRouteAndBusOwner(discountDate,routeId,busOwnerId);
    }
}
