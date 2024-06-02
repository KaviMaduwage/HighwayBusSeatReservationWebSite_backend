package com.project.seatReservation.dao;

import com.project.seatReservation.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiscountDao extends JpaRepository<Discount,Integer> {
    @Query("SELECT d FROM Discount d WHERE d.discountId = :discountId")
    Discount getDiscountById(int discountId);

    @Query("SELECT d FROM Discount d WHERE (0 = :routeId OR d.route.routeId = :routeId) AND (0 = :busOwnerId OR d.busOwner.busOwnerId = :busOwnerId) AND ('' = :discountDate OR d.discountStartDate >= DATE(:discountDate)) OR d.discountExpiryDate >= DATE(:discountDate) ")
    List<Discount> findDiscountsByDateRouteAndBusOwner(String discountDate, int routeId, int busOwnerId);
}
