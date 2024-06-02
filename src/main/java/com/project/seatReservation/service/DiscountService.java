package com.project.seatReservation.service;

import com.project.seatReservation.model.Discount;

import java.util.List;

public interface DiscountService {
    void saveDiscount(Discount discount);

    List<Discount> getAllDiscounts();

    Discount getDiscountById(int discountId);

    List<Discount> findDiscountsByDateRouteAndBusOwner(String dateStr, int routeId, int busOwnerId);
}
