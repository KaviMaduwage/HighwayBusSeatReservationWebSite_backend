package com.project.seatReservation.service;

import com.project.seatReservation.model.Request;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RequestService {
    Request saveRequest(Request request);

    List<Request> getAllRequests();

    Request findRequestById(int requestId);

    void updateRequest(Request request);
}
