package com.project.seatReservation.service;

import com.project.seatReservation.model.FeedBack;
import com.project.seatReservation.model.FeedBackGrading;

import java.util.List;

public interface FeedBackService {
    List<FeedBackGrading> getFeedBackGradings();

    void saveFeedBack(FeedBack feedBack);

    List<FeedBack> getAllFeedbacks();
}
