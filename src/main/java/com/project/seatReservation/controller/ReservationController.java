package com.project.seatReservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.seatReservation.model.*;
import com.project.seatReservation.model.report.TicketTemplate;
import com.project.seatReservation.response.PaymentResponse;
import com.project.seatReservation.service.*;
import com.stripe.exception.StripeException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin
public class ReservationController {
    UserService userService;
    ScheduleService scheduleService;
    ReservationService reservationService;
    PaymentService paymentService;
    ReportService reportService;

    PassengerService passengerService;
    BusService busService;

    DiscountService discountService;

    BusOwnerService busOwnerService;
    BusCrewService busCrewService;

    public ReservationController(UserService userService, ScheduleService scheduleService,
                                 ReservationService reservationService,PaymentService paymentService,
                                 ReportService reportService,PassengerService passengerService,BusService busService,
                                 BusOwnerService busOwnerService,BusCrewService busCrewService,DiscountService discountService) {
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.reportService = reportService;
        this.passengerService = passengerService;
        this.busService = busService;
        this.busCrewService= busCrewService;
        this.busOwnerService = busOwnerService;
        this.discountService = discountService;
    }

    @RequestMapping(value = "/findBlockedSeatsByScheduleId", method = RequestMethod.POST)
    public ResponseEntity<?> findBlockedSeatsByScheduleId(@RequestBody Map<String, Integer> requestBody){
        int scheduleId = requestBody.get("scheduleId");

        List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatsByScheduleId(scheduleId);

        return ResponseEntity.ok().body(blockedSeats.toArray());
    }

    @RequestMapping(value = "/findReservedSeatsByScheduleId", method = RequestMethod.POST)
    public ResponseEntity<?> findReservedSeatsByScheduleId(@RequestBody Map<String, Integer> requestBody){
        int scheduleId = requestBody.get("scheduleId");

        List<SeatReservation> bookedSeats = reservationService.findReservedSeatsByScheduleId(scheduleId);

        return ResponseEntity.ok().body(bookedSeats.toArray());
    }

    @RequestMapping(value = "/blockSeat", method = RequestMethod.POST)
    public ResponseEntity<?> blockSeat(@RequestBody Map<String, Integer> requestBody){

        int userId = requestBody.get("userId");
        int scheduleId = requestBody.get("scheduleId");
        int rowNo = requestBody.get("row");
        int colNo = requestBody.get("col");

        User user = userService.findUserByUserId(userId);
        List<Schedule> schedule = scheduleService.findScheduleById(scheduleId);

        BlockedSeat blockedSeat = new BlockedSeat();
        blockedSeat.setUser(user);
        blockedSeat.setSchedule(schedule.get(0));
        blockedSeat.setRow(rowNo);
        blockedSeat.setCol(colNo);

        BlockedSeat savedBlockedSeat = reservationService.saveBlockedSeat(blockedSeat);

        List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatsByScheduleId(scheduleId);

        return ResponseEntity.ok().body(blockedSeats.toArray());
    }

    @RequestMapping(value = "/unblockSelectedSeat", method = RequestMethod.POST)
    public ResponseEntity<?> unblockSelectedSeat(@RequestBody Map<String, Integer> requestBody){

        int userId = requestBody.get("userId");
        int scheduleId = requestBody.get("scheduleId");
        int rowNo = requestBody.get("row");
        int colNo = requestBody.get("col");


        BlockedSeat savedBlockedSeat = reservationService.findBlockedSeatByScheduleIdUserIdRowAndCol(scheduleId,rowNo,colNo,userId);
        if(savedBlockedSeat != null){
            reservationService.deleteBlockedSeat(savedBlockedSeat);
        }
        List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatsByScheduleId(scheduleId);

        return ResponseEntity.ok().body(blockedSeats.toArray());
    }

    @RequestMapping(value = "/addReservationToCart",method = RequestMethod.POST)
    public ResponseEntity<String> addReservationToCart(@RequestBody Map<String,String> requestBody){

        String message = "";

        int userId = Integer.parseInt(requestBody.get("userId"));
        int scheduleId = Integer.parseInt(requestBody.get("reserveScheduleId"));
        String pickUpPoint = requestBody.get("pickUpPoint");
        String dropOffPoint = requestBody.get("dropOffPoint");
        String remark = requestBody.get("remark");

        List<Schedule> scheduleList = scheduleService.findScheduleById(scheduleId);
        User user = userService.findUserByUserId(userId);

        if(scheduleList != null && !scheduleList.isEmpty()){

            List<BlockedSeat> blockedSeats = reservationService.findBlockedSeatsByScheduleIdAndUserId(scheduleList.get(0).getScheduleId(), userId);

            List<Cart> cartList = reservationService.findCartByScheduleIdAndUserId(scheduleList.get(0).getScheduleId(), userId);

            if(cartList.isEmpty()){
                if(blockedSeats != null && blockedSeats.size()>0 ){
                    String blockSeatsStr = "";
                    String blockSeatIdsStr = "";
                    int seatCount = 0;

                    for(BlockedSeat blockedSeat : blockedSeats){
                        blockSeatsStr += blockedSeat.getRow()+" - "+blockedSeat.getCol()+",";
                        blockSeatIdsStr += blockedSeat.getBlockedSeatId()+",";
                        seatCount++;
                    }

                    blockSeatsStr = blockSeatsStr.substring(0,blockSeatsStr.length()-1).trim();
                    blockSeatIdsStr = blockSeatIdsStr.substring(0,blockSeatIdsStr.length()-1).trim();


                    Cart cart = new Cart();
                    cart.setSchedule(scheduleList.get(0));
                    cart.setUser(user);
                    cart.setDropPoint(dropOffPoint);
                    cart.setPickUpPoint(pickUpPoint);
                    cart.setRemark(remark);
                    cart.setSeatNos(blockSeatsStr);
                    cart.setBlockedSeatIds(blockSeatIdsStr);
                    cart.setPrice(seatCount*scheduleList.get(0).getTicketPrice());



                    Cart savedCart = reservationService.saveCart(cart);
                    List<CartAddedBlockedSeat> cartAddedBlockedSeatList = new ArrayList<>();
                    for(BlockedSeat blockedSeat : blockedSeats){
                        CartAddedBlockedSeat cartAddedBlockedSeat = new CartAddedBlockedSeat();
                        cartAddedBlockedSeat.setCart(cart);
                        cartAddedBlockedSeat.setBlockedSeat(blockedSeat);

                        cartAddedBlockedSeatList.add(cartAddedBlockedSeat);
                    }

                    reservationService.saveCartAddedBlockedSeats(cartAddedBlockedSeatList);


                    message = "Successfully added to the cart. Proceed with other reservations.";


                }else{
                    message = "No selected seats to add the cart. Time limit has exceeded.";
                }

            }else{
                message = "You have already added to the cart. Please delete the cart if you want to change the seats.";
            }

        }else{
            message = "Schedule is currently unavailable.";
        }

        return ResponseEntity.ok().body(message);
    }
    @RequestMapping(value = "/loadCartListByUserId",method = RequestMethod.POST)
    public ResponseEntity<?> loadCartListByUserId(@RequestBody Map<String, Integer> requestBody){
        List<Cart> cartList = new ArrayList<>();
        int userId = requestBody.get("userId");

        cartList = reservationService.findCartDetailsByUserId(userId);

        List<Map<String, Object>> responseData = new ArrayList<>();

        for(Cart c : cartList){
            Map<String, Object> rowData = new HashMap<>();
            rowData.put("cart",c);

            int routeId = c.getSchedule().getBus().getRoute().getRouteId();
            String tripDateStr = c.getSchedule().getTripDateStr();
            int busOwnerId = c.getSchedule().getBus().getBusOwner().getBusOwnerId();

            List<Discount> discounts = discountService.findDiscountsByDateRouteAndBusOwner(tripDateStr,routeId,busOwnerId);
            rowData.put("discounts",discounts);

            responseData.add(rowData);
        }

        return ResponseEntity.ok().body(responseData);
    }

    @RequestMapping(value = "/deleteCartByCartId", method = RequestMethod.POST)
    public ResponseEntity<?> deleteCartByCartId(@RequestBody Map<String, Integer> requestBody){
        String message = "";
        int cartId = requestBody.get("cartId");
        List<Integer> cartIdList = new ArrayList<>();
        cartIdList.add(cartId);

        reservationService.deleteCartList(cartIdList);

        List<Cart> cartList = reservationService.findCartById(cartId);
        if(cartList.isEmpty()){
            message = "Successfully deleted.";
        }else{
            message = "Can't delete.";
        }
        return ResponseEntity.ok().body(message);
    }

    @RequestMapping(value = "/makeReservation", method = RequestMethod.POST)
    public ResponseEntity<PaymentResponse> makeReservation(@RequestBody Map<String,Object> requestBody) throws StripeException {

        double price = ((Number) requestBody.get("totalPrice")).doubleValue();
        int userId = (int) requestBody.get("userId");
        User user = userService.findUserByUserId(userId);

        //List<Cart> cartList = (ArrayList<Cart>) requestBody.get("cartList");
        List<Map<String, Object>> cartMapList = (List<Map<String, Object>>) requestBody.get("cartList");
        List<Cart> cartList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        List<Passenger> passengerList = passengerService.findPassengerByUserId(userId);
        Wallet wallet = paymentService.findWalletByPassengerId(passengerList.get(0).getPassengerId());
        if(wallet != null){
            double walletAmount = wallet.getAmount();
            if(walletAmount < price){
                price = price - walletAmount;
                wallet.setAmount(0);
                paymentService.updateWallet(wallet);
            }
        }



        for (Map<String, Object> cartMap : cartMapList) {
            //Cart cart = new Cart();
            Cart cart = objectMapper.convertValue(cartMap.get("cart"), Cart.class);

//            cart.setCartId((Integer) cartMap.get("cartId"));
//            cart.setPickUpPoint((String) cartMap.get("pickUpPoint"));
//            cart.setDropPoint((String) cartMap.get("dropPoint"));
//            cart.setRemark((String) cartMap.get("remark"));
//            cart.setPrice(((Number) cartMap.get("price")).doubleValue());
//            cart.setUser(user);
//            cart.setSchedule((Schedule) cartMap.get("schedule"));
//            cart.setBlockedSeatIds((String) cartMap.get("blockedSeatIds"));
//            cart.setSeatNos((String) cartMap.get("seatNos"));


            cartList.add(cart);
        }
        List<Integer> reservationIdList = createReservationTableRecords(cartList,userId);
        PaymentResponse paymentResponse = paymentService.createPaymentLink(userId,price,reservationIdList);

        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);

    }
    @RequestMapping(value = "/makeReservationFromWalletSavings", method = RequestMethod.POST)
    public ResponseEntity<?> makeReservationFromWalletSavings(@RequestBody Map<String,Object> requestBody){
        String message = "";
        double price = ((Number) requestBody.get("totalPrice")).doubleValue();
        int userId = (int) requestBody.get("userId");
        List<Passenger> passengerList = passengerService.findPassengerByUserId(userId);

        List<Map<String, Object>> cartMapList = (List<Map<String, Object>>) requestBody.get("cartList");
        List<Cart> cartList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();


        Wallet wallet = paymentService.findWalletByPassengerId(passengerList.get(0).getPassengerId());
        wallet.setAmount(wallet.getAmount() - price);
        paymentService.updateWallet(wallet);


        for (Map<String, Object> cartMap : cartMapList) {
            Cart cart = objectMapper.convertValue(cartMap.get("cart"), Cart.class);
            cartList.add(cart);
        }

        List<Reservation> newReservations = new ArrayList<>();
        List<SeatReservation> seatReservationList = new ArrayList<>();
        List<Passenger> passengers = passengerService.findPassengerByUserId(userId);

        for(Cart c : cartList){

            Reservation reservation = new Reservation();
            reservation.setPaymentCompleted(true);
            reservation.setCancelled(false);
            reservation.setBoardingPoint(c.getPickUpPoint());
            reservation.setDroppingPoint(c.getDropPoint());
            reservation.setPassenger(passengers.get(0));
            reservation.setSchedule(c.getSchedule());

            Reservation savedReservation = reservationService.saveReservation(reservation);

            newReservations.add(savedReservation);

            String blockedSeatIdsStr = c.getBlockedSeatIds();
            String[] blockedSeatIds = blockedSeatIdsStr.split(",");

            String seatNoStr = c.getSeatNos();
            String[] seatNos = seatNoStr.split(",");
            int busId = c.getSchedule().getBus().getBusId();
            List<Seat> seatList = busService.getSeatsBySeatNoStr(seatNos,busId);

            for(Seat s : seatList){
                SeatReservation sr = new SeatReservation();
                sr.setReservation(savedReservation);
                sr.setSeat(s);
                sr.setStatus("Success");

                seatReservationList.add(sr);
            }

        }

        if(!seatReservationList.isEmpty()){
            reservationService.saveSeatReservations(seatReservationList);
        }

        //delete cart data by user id
        deleteCartData(userId);

        message = "Seats are successfully reserved. Go to Profile->My Reservations to download the ticket.";

        return ResponseEntity.ok().body(message);
    }


    private void deleteCartData(int userId) {
        reservationService.deleteCartDataByUserId(userId);
    }

    private List<Integer> createReservationTableRecords(List<Cart> cartList,int userId) {
        List<Integer> reservationIdList = new ArrayList<>();
        List<Reservation> newReservations = new ArrayList<>();
        List<SeatReservation> seatReservationList = new ArrayList<>();
        List<Passenger> passengers = passengerService.findPassengerByUserId(userId);


        for(Cart c : cartList){

            Reservation reservation = new Reservation();
            reservation.setPaymentCompleted(false);
            reservation.setCancelled(false);
            reservation.setBoardingPoint(c.getPickUpPoint());
            reservation.setDroppingPoint(c.getDropPoint());
            reservation.setPassenger(passengers.get(0));
            reservation.setSchedule(c.getSchedule());

            Reservation savedReservation = reservationService.saveReservation(reservation);

            newReservations.add(savedReservation);

            String blockedSeatIdsStr = c.getBlockedSeatIds();
            String[] blockedSeatIds = blockedSeatIdsStr.split(",");

            String seatNoStr = c.getSeatNos();
            String[] seatNos = seatNoStr.split(",");
            int busId = c.getSchedule().getBus().getBusId();
            List<Seat> seatList = busService.getSeatsBySeatNoStr(seatNos,busId);

            for(Seat s : seatList){
                SeatReservation sr = new SeatReservation();
                sr.setReservation(savedReservation);
                sr.setSeat(s);
                sr.setStatus("Pending");

                seatReservationList.add(sr);
            }

        }

        if(!seatReservationList.isEmpty()){
            reservationService.saveSeatReservations(seatReservationList);
        }

        if(!newReservations.isEmpty()){
            for(Reservation r : newReservations){
                reservationIdList.add(r.getReservationId());
            }
        }


        return reservationIdList;
    }

    @RequestMapping(value = "/payment/success", method = RequestMethod.GET)
    private void handlePaymentSuccess(@RequestParam String sessionId) {
        // Method logic goes here
        System.out.println("Payment success method invoked.");
    }

    @RequestMapping(value = "/generateTicket",method = RequestMethod.GET)
    public ResponseEntity<byte[]> generateTicket(@RequestParam("reservationId") Integer reservationId) {

        try {
            String jrxmlTemplateName = "ticket.jrxml";

            List<TicketTemplate> ticketTemplateList = new ArrayList<>();

            Reservation reservation = reservationService.findReservationByRevId(reservationId);
            List<SeatReservation> seatReservationList = reservationService.findReservedSeatsByRevId(reservationId);

            int busOwnerId = reservation.getSchedule().getBus().getBusOwner().getBusOwnerId();
            String tripDateStr = reservation.getSchedule().getTripDateStr();
            int routeId = reservation.getSchedule().getBus().getRoute().getRouteId();

            List<Discount> discounts = discountService.findDiscountsByDateRouteAndBusOwner(tripDateStr,routeId,busOwnerId);

            TicketTemplate ticketTemplate = new TicketTemplate();
            ticketTemplate.setBusNo(reservation.getSchedule().getBus().getPlateNo());
            ticketTemplate.setOrigin(reservation.getSchedule().getOrigin());
            ticketTemplate.setDestination(reservation.getSchedule().getDestination());
            ticketTemplate.setTravelDate(reservation.getSchedule().getTripDateStr());
            ticketTemplate.setTravelTime(reservation.getSchedule().getTripStartTime());
            ticketTemplate.setNic(reservation.getPassenger().getNic());

            double discountAmount = 0;
            double amountAfterDiscounts = 0.0;
            String discountDes = "(";
            double ticketPrice = 0;
            double amount =0.0;
            String seatStr = "";
            for(SeatReservation sr: seatReservationList){
                ticketPrice = sr.getReservation().getSchedule().getTicketPrice();
                amount += sr.getReservation().getSchedule().getTicketPrice();
                seatStr += sr.getSeat().getRowNo()+" - "+ sr.getSeat().getColumnNo()+",";
            }

            for(Discount d:discounts){
                discountAmount += ticketPrice * d.getPercentage()/100;
                discountDes += d.getDiscountName()+", ";
            }

            if(!discountDes.equals("(")){
                discountDes = discountDes.substring(0,discountDes.length()-2).trim()+")";
            }
            amountAfterDiscounts = amount - discountAmount* seatReservationList.size();
            ticketTemplate.setAmountAfterDiscount(String.valueOf(amountAfterDiscounts));

            seatStr = seatStr.substring(0,seatStr.length()-1);
            ticketTemplate.setSeatNos(seatStr);
            ticketTemplate.setPrice(String.valueOf(amount));
            ticketTemplate.setDiscountDes(discountDes);
            ticketTemplate.setDiscountAmount(String.valueOf(discountAmount* seatReservationList.size()));

            ticketTemplateList.add(ticketTemplate);

            JRDataSource dataSource = new JRBeanCollectionDataSource(ticketTemplateList);
            Map<String, Object> parameters = new HashMap<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            String generatedDate = sdf.format(new Date());
            parameters.put("generatedDate", generatedDate);

            String sinhalaAcknowledgement = "ඔබ මෙය ඉදිරිපත් කර ඔබ බසයට ගොඩ වූ පසු කොන්දොස්තරගෙන් ටිකට් පත ලබා ගත යුතුය.";
            parameters.put("sinhalaAcknowledgement",sinhalaAcknowledgement);

            byte[] pdfBytes = reportService.generatePDF(jrxmlTemplateName, dataSource, parameters);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ticket.pdf");

            // Return the PDF file as a byte array
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException | JRException e) {

            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findReservationsByUserId",method = RequestMethod.POST)
    public ResponseEntity<?> findReservationsByUserId(@RequestBody Map<String,Integer> requestBody){
        List<Reservation> reservationList = new ArrayList<>();

        int userId = requestBody.get("userId");

        reservationList = reservationService.findReservationsByUserId(userId);

        List<Map<String, Object>> responseData = new ArrayList<>();

        for(Reservation reservation : reservationList){
            Map<String, Object> s = new HashMap<>();
            s.put("reservation",reservation);

            List<SeatReservation> seatReservationList = reservationService.findSuccessReservedSeatsByRevId(reservation.getReservationId());
            s.put("noOfSeats",seatReservationList.size());

            responseData.add(s);
        }

        return ResponseEntity.ok().body(responseData);
    }

    @RequestMapping(value = "/findReservedSeatsByReservationId", method = RequestMethod.POST)
    public ResponseEntity<?> findReservedSeatsByReservationId(@RequestBody Map<String,Integer> requestBody){
        List<SeatReservation> seatList = new ArrayList<>();

        int reservationId = requestBody.get("reservationId");
        seatList = reservationService.findSuccessReservedSeatsByRevId(reservationId);

        return ResponseEntity.ok().body(seatList.toArray());
    }

    @RequestMapping(value = "/cancelReservations", method = RequestMethod.POST)
    public ResponseEntity<?> cancelReservations(@RequestBody Map<String,Object> requestBody){
        String message = "";
        String type = (String) requestBody.get("type");
        List<String> cancelSeatListStr = (List<String>) requestBody.get("cancelSeatList");
        List<Integer> cancelSeatList = new ArrayList<>();
        for(String s : cancelSeatListStr){
            cancelSeatList.add(Integer.valueOf(s));
        }
        int userId = (Integer) requestBody.get("userId");

        if(type.equals("wallet")){
            message = manageWallet(cancelSeatList,userId);

        }else if(type.equals("refund")){

        }


        return ResponseEntity.ok().body(message);
    }

    private String manageWallet(List<Integer> cancelSeatList, int userId) {
        String message = "";

        List<SeatReservation> seatReservationList = reservationService.findSeatReservationsBYId(cancelSeatList);
        Passenger passenger = passengerService.findPassengerByUserId(userId).get(0);

        if(seatReservationList.size() > 0){
            int reservationId = seatReservationList.get(0).getReservation().getReservationId();
            int maxSeatReservations = (reservationService.findReservedSeatsByReservationId(reservationId)).size();

            double ticketPricePerPerson = seatReservationList.get(0).getReservation().getSchedule().getTicketPrice();
            double totalRefund = ticketPricePerPerson * seatReservationList.size();

            Reservation reservation = reservationService.findReservationByRevId(reservationId);

            if(seatReservationList.size() == maxSeatReservations){

                reservation.setCancelled(true);
                reservationService.updateReservation(reservation);

                Payment toBeUpdatePayment = reservation.getPayment();
                toBeUpdatePayment.setActive(false);
                paymentService.updatePayment(toBeUpdatePayment);

            }else{
                Payment toBeUpdatePayment = reservation.getPayment();
                double validAmount = toBeUpdatePayment.getAmount() - totalRefund;
                toBeUpdatePayment.setAmount(validAmount);
                paymentService.updatePayment(toBeUpdatePayment);
            }

            List<SeatReservation> toBeUpdateSeatReservations = new ArrayList<>();
            for(SeatReservation sr : seatReservationList){
                sr.setStatus("Cancelled");
                toBeUpdateSeatReservations.add(sr);
            }

            reservationService.updateSeatReservations(toBeUpdateSeatReservations);

            // create wallet
            Wallet wallet = paymentService.findWalletByPassengerId(passenger.getPassengerId());
            double currentAmount = 0;
            if(wallet != null){
                //update existing wallet
                currentAmount += wallet.getAmount() + totalRefund;
                wallet.setAmount(currentAmount);
                paymentService.updateWallet(wallet);
            }else{
                //create new wallet
                Wallet newWallet = new Wallet();
                newWallet.setAmount(totalRefund);
                newWallet.setPassenger(passenger);
                paymentService.saveWallet(newWallet);
            }

            message = "Successfully cancel the ticket/s and added money to the wallet.";

        }else{
            message = "Error in reservation cancellation process.";
        }
        return message;
    }

    @RequestMapping(value = "/getUpcomingReservationsByUserId", method = RequestMethod.POST)
    public ResponseEntity<?> getUpcomingReservationsByUserId(@RequestBody Map<String,Integer> requestBody){


        int noOfReservations = 0;
        int totalCancellations = 0;
        int userId = requestBody.get("userId");
        Map<String,Integer> resultMap = new HashMap<>();
        List<SeatReservation> seatReservationList = reservationService.getUpcomingReservationsByUserId(userId);
        List<SeatReservation> cancelledSeatReservationList = reservationService.getCancelledReservations(userId);

        if(seatReservationList != null && seatReservationList.size() > 0){
            noOfReservations = seatReservationList.size();
        }

        if(cancelledSeatReservationList != null && cancelledSeatReservationList.size() > 0){
            totalCancellations = cancelledSeatReservationList.size();
        }

        resultMap.put("noOfReservations",noOfReservations);
        resultMap.put("totalCancellations",totalCancellations);

        return ResponseEntity.ok().body(resultMap);

    }

}
