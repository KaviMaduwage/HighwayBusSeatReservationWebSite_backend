package com.project.seatReservation.service;

import com.project.seatReservation.dao.RequestDao;
import com.project.seatReservation.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService{

    RequestDao requestDao;

    public RequestServiceImpl(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    @Override
    public Request saveRequest(Request request) {
        return requestDao.save(request);
    }
}
