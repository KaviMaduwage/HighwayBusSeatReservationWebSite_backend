package com.project.seatReservation.dao;

import com.project.seatReservation.model.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedBackDao extends JpaRepository<FeedBack,Integer> {

    @Query("SELECT f FROM FeedBack f ORDER BY f.createdDate DESC")
    List<FeedBack> getAllFeedbacks();
}
