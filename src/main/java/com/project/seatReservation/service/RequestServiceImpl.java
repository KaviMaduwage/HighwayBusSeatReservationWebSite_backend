package com.project.seatReservation.service;

import com.project.seatReservation.dao.RequestDao;
import com.project.seatReservation.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

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

    @Override
    public List<Request> getAllRequests() {
        return requestDao.findAll();
    }

    @Override
    public Request findRequestById(int requestId) {
        return requestDao.findRequestById(requestId);
    }

    @Override
    @Transactional
    public void updateRequest(Request request) {
        requestDao.save(request);
    }
}
