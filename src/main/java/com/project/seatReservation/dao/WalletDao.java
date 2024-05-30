package com.project.seatReservation.dao;

import com.project.seatReservation.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WalletDao extends JpaRepository<Wallet,Integer> {
    @Query("SELECT w FROM Wallet w WHERE w.passenger.passengerId = :passengerId")
    Wallet findWalletByPassengerId(int passengerId);
}
