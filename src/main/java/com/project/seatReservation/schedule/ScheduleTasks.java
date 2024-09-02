package com.project.seatReservation.schedule;

import com.project.seatReservation.model.*;
import com.project.seatReservation.service.EmailService;
import com.project.seatReservation.service.NotificationService;
import com.project.seatReservation.service.ReservationService;
import com.project.seatReservation.service.ScheduleService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.Duration;

@EnableScheduling
@Component
public class ScheduleTasks {
    ReservationService reservationService;
    ScheduleService scheduleService;
    EmailService emailService;
    NotificationService notificationService;

    public ScheduleTasks(ReservationService reservationService,ScheduleService scheduleService,
                         EmailService emailService,NotificationService notificationService) {
        this.reservationService = reservationService;
        this.scheduleService = scheduleService;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void deleteBlockedSeatRecords(){
        //delete only blocked seats
        List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatRecordsWithinThirtyMin();
        List<Integer> idList = new ArrayList<>();
        List<Integer> cartAddedSeatIdList = new ArrayList<>();
        for(BlockedSeat blockedSeat : blockedSeats){
            idList.add(blockedSeat.getBlockedSeatId());
        }

        List<BlockedSeat> cartAddedBlockedSeatList = reservationService.findCartAddedBlockedSeats(idList);
        List<BlockedSeat> onlyBlockedSeatList = new ArrayList<>();

        for(BlockedSeat b : cartAddedBlockedSeatList){
            cartAddedSeatIdList.add(b.getBlockedSeatId());
        }

        for(BlockedSeat b : blockedSeats){
            if(!cartAddedSeatIdList.contains(b.getBlockedSeatId())){
                onlyBlockedSeatList.add(b);
            }
        }
        reservationService.deleteBlockedSeatList(onlyBlockedSeatList);

        // delete card added blocked seats when cart added time exceeds given time

        List<Cart> cartList = reservationService.findCartSeatRecordsWithinThirtyMin();
        List<Integer> cartIds = new ArrayList<>();
        for(Cart c : cartList){
            cartIds.add(c.getCartId());
        }

        reservationService.deleteCartList(cartIds);
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void sendNotifications(){
        LocalTime currentTime = LocalTime.now();
        //LocalTime timeBeforeTwoHours = currentTime.minusHours(2);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        //String timeBeforeTwoHoursStr = timeBeforeTwoHours.format(dateTimeFormatter);
        Date currentDate = new Date();

        List<Schedule> scheduleList = scheduleService.findScheduleByDate(currentDate);
        for(Schedule s : scheduleList){
            List<Integer> notifiedPassengerList = new ArrayList<>();
            LocalTime tripStartLocalTime = LocalTime.parse(s.getTripStartTime(),dateTimeFormatter);
            LocalTime twoHourseBeforeTripStartLocalTime = tripStartLocalTime.minusHours(2);

            Duration duration = Duration.between(currentTime, tripStartLocalTime);

            if(currentTime.isBefore(tripStartLocalTime) && duration.toHours() < 2 && !s.getIsNotified()){

                // notify passengers
                List<SeatReservation> reservationList = reservationService.findReservedSeatsByScheduleId(s.getScheduleId());
                for(SeatReservation sr : reservationList){
                    int passengerId = sr.getReservation().getPassenger().getPassengerId();
                    if(!notifiedPassengerList.contains(passengerId)){
                        notifiedPassengerList.add(passengerId);
                        notifyPassengerAboutTrip(sr,sr.getReservation().getPassenger());
                    }
                }

                //notify bus owners
                notifyBusCrew(s,reservationList);

                s.setIsNotified(true);
                scheduleService.updateSchedule(s);


            }
        }
    }

    private void notifyBusCrew(Schedule s, List<SeatReservation> reservationList) {
        String toAddress = s.getBus().getBusOwner().getUser().getEmail();

        String availableSeatsStr = getBookedSeatAmountByScheduleId(s.getScheduleId());
        String start = s.getOrigin();
        String end = s.getDestination();
        String busPlateNo = s.getBus().getPlateNo();

        String fromAddress = "myseatofficial@gmail.com";
        String senderName = "My Seat";
        String subject = "My Seat Reservation";
        String content = "Please note.\n\n" +
                "Regarding schedule from "+start+" to "+end+"\n" +
                "Bus plate no - "+busPlateNo+"\n" +
                "Occupied seats -"+availableSeatsStr;

        emailService.sendEmails(toAddress, fromAddress, senderName, subject, content);

        Notification notification = new Notification();
        notification.setNote(content);
        notification.setIsViewed(false);
        notification.setCreatedDate(new Date());
        notification.setUser(s.getBus().getBusOwner().getUser());

        notificationService.saveNotification(notification);
    }

    private String getBookedSeatAmountByScheduleId(int scheduleId) {
        int availableSeat = 0;
        String availableSeatsStr = "";



        List<Schedule> scheduleList = scheduleService.findScheduleById(scheduleId);

        if(scheduleList != null && !scheduleList.isEmpty()){
            Schedule s = scheduleList.get(0);
            int allSeats = s.getBus().getNoOfSeats();
            List<Reservation> reservationList = reservationService.findReservationsByScheduleId(scheduleId);

            availableSeat = allSeats - reservationList.size();
            availableSeatsStr = reservationList.size() +"/"+allSeats;
        }




        return availableSeatsStr;
    }

    private void notifyPassengerAboutTrip(SeatReservation sr, Passenger passenger) {
        String start = sr.getReservation().getSchedule().getOrigin();
        String end = sr.getReservation().getSchedule().getDestination();
        String busPlateNo = sr.getReservation().getSchedule().getBus().getPlateNo();
        String conductorNo = "N/A";

        List<TripCrew> conductors = scheduleService.getConductorDetailsByScheduleId(sr.getReservation().getSchedule().getScheduleId());
        if(conductors != null && conductors.size() > 0){
            conductorNo = conductors.get(0).getBusCrew().getMobileNo();
        }

        String toAddress = passenger.getUser().getEmail();
        String fromAddress = "myseatofficial@gmail.com";
        String senderName = "My Seat";
        String subject = "My Seat Reservation";
        String content = "Please note.\n\n" +
                "Regarding reservation from "+start+" to "+end+"\n" +
                "Bus plate no - "+busPlateNo+"\n" +
                "Conductor No -"+conductorNo;



        emailService.sendEmails(toAddress, fromAddress, senderName, subject, content);

        Notification notification = new Notification();
        notification.setNote(content);
        notification.setIsViewed(false);
        notification.setCreatedDate(new Date());
        notification.setUser(passenger.getUser());

        notificationService.saveNotification(notification);
    }

}
