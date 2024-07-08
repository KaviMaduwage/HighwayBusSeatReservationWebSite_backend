package com.project.seatReservation.service;

import com.project.seatReservation.dao.FeedBackDao;
import com.project.seatReservation.dao.FeedBackGradingDao;
import com.project.seatReservation.model.FeedBack;
import com.project.seatReservation.model.FeedBackGrading;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedBackServiceImpl implements FeedBackService{

    FeedBackDao feedBackDao;
    FeedBackGradingDao feedBackGradingDao;

    public FeedBackServiceImpl(FeedBackDao feedBackDao, FeedBackGradingDao feedBackGradingDao) {
        this.feedBackDao = feedBackDao;
        this.feedBackGradingDao = feedBackGradingDao;
    }

    @Override
    public List<FeedBackGrading> getFeedBackGradings() {
        return feedBackGradingDao.findAll();
    }

    @Override
    public void saveFeedBack(FeedBack feedBack) {
        feedBackDao.save(feedBack);
    }

    @Override
    public List<FeedBack> getAllFeedbacks() {
        return feedBackDao.getAllFeedbacks();
    }
}
