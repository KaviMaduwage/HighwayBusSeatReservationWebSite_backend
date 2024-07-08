package com.project.seatReservation.controller;

import com.project.seatReservation.model.Bus;
import com.project.seatReservation.model.FeedBack;
import com.project.seatReservation.model.FeedBackGrading;
import com.project.seatReservation.service.BusService;
import com.project.seatReservation.service.FeedBackService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class FeedBackController {
    FeedBackService feedBackService;

    BusService busService;

    public FeedBackController(FeedBackService feedBackService, BusService busService) {
        this.feedBackService = feedBackService;
        this.busService = busService;
    }

    @RequestMapping(value = "/saveFeedBack", method = RequestMethod.POST)
    public ResponseEntity<?> saveFeedBack(@RequestBody Map<String,Object> requestBody) throws ParseException {
        String message  = "";
        Map<String,String> feedbackMap = (Map<String, String>) requestBody.get("feedback");
        String busNo = feedbackMap.get("busNo");
        String travelDateStr = feedbackMap.get("travelDate");
        String cleanliness = feedbackMap.get("cleanliness");
        String timing = feedbackMap.get("timing");
        String driverBehaviour = feedbackMap.get("driverBehaviour");
        String conductorBehaviour = feedbackMap.get("conductorBehaviour");
        String otherComments = feedbackMap.get("otherComments");

        List<FeedBackGrading> feedBackGradings = feedBackService.getFeedBackGradings();
        Map<String,Integer> feedBackGradingMap = new HashMap<>();

        for(FeedBackGrading fg : feedBackGradings){
            feedBackGradingMap.put(fg.getDescription() , fg.getFeedBackGradingId());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date travelDate = sdf.parse(travelDateStr);

        List<Bus> busList = busService.findBusByPlateNo(busNo);
        if(busList != null && busList.size() > 0){
            FeedBack feedBack = new FeedBack();
            feedBack.setBus(busList.get(0));
            feedBack.setCleanlinessGradeId(feedBackGradingMap.get(cleanliness));
            feedBack.setConductorGradeId(feedBackGradingMap.get(conductorBehaviour));
            feedBack.setDriverGradeId(feedBackGradingMap.get(driverBehaviour));
            feedBack.setTimingGradeId(feedBackGradingMap.get(timing));
            feedBack.setOtherComments(otherComments);
            feedBack.setTravelDate(travelDate);
            feedBack.setCreatedDate(new Date());

            feedBackService.saveFeedBack(feedBack);
            message = "Successfully saved the feedback";
        }else{
            message = "Bus No is not valid.";
        }
        return ResponseEntity.ok().body(message);
    }
    @RequestMapping(value = "/getAllFeedbacks", method = RequestMethod.POST)
    public ResponseEntity<?> getAllFeedbacks(){
        List<FeedBack> feedBacks = feedBackService.getAllFeedbacks();

        return ResponseEntity.ok().body(feedBacks.toArray());
    }
}
