package com.project.seatReservation.controller;

import com.google.gson.JsonSyntaxException;
import com.project.seatReservation.model.*;
import com.project.seatReservation.service.PaymentService;
import com.project.seatReservation.service.ReservationService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
public class StripeWebHookController {

    ReservationService reservationService;
    PaymentService paymentService;

    public StripeWebHookController(ReservationService reservationService,PaymentService paymentService) {
        this.reservationService = reservationService;
        this.paymentService = paymentService;
    }

    private Logger logger = LoggerFactory.getLogger(StripeWebHookController.class);

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

//    @PostMapping("/stripe/events")
//    public String handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){
//
//        // Replace this endpoint secret with your endpoint's unique secret
//        // If you are testing with the CLI, find the secret by running 'stripe listen'
//        // If you are using an endpoint defined with the API or dashboard, look in your webhook settings
//        // at https://dashboard.stripe.com/webhooks
//
//        Event event;
//
//            if(endpointSecret != null && sigHeader != null) {
//                // Only verify the event if you have an endpoint secret defined.
//                // Otherwise use the basic event deserialized with GSON.
//                try {
//                    event = Webhook.constructEvent(
//                            payload, sigHeader, endpointSecret
//                    );
//                } catch (SignatureVerificationException e) {
//                    // Invalid signature
//                    logger.info("⚠️  Webhook error while validating signature."+e);
//                    return "";
//                }
//            }else {
//                return "";
//            }
//            logger.info("passed");
//            // Deserialize the nested object inside the event
//            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
//            StripeObject stripeObject = null;
//            if (dataObjectDeserializer.getObject().isPresent()) {
//                stripeObject = dataObjectDeserializer.getObject().get();
//            } else {
//                // Deserialization failed, probably due to an API version mismatch.
//                // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
//                // instructions on how to handle this case, or return an error here.
//            }
//            // Handle the event
//            switch (event.getType()) {
//                case "payment_intent.succeeded":
//                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
//                    logger.info("Payment for " + paymentIntent.getAmount() + " succeeded.");
//                    // Then define and call a method to handle the successful payment intent.
//                    // handlePaymentIntentSucceeded(paymentIntent);
//                    break;
//                case "payment_method.attached":
//                    PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
//                    // Then define and call a method to handle the successful attachment of a PaymentMethod.
//                    // handlePaymentMethodAttached(paymentMethod);
//                    break;
//                default:
//                    logger.warn("Unhandled event type: " + event.getType());
//                    break;
//            }
//
//            return "";
//
//
//    }

    @PostMapping("/stripe/events")
    public String handleStripeEvent(@RequestBody String payload){

        // Replace this endpoint secret with your endpoint's unique secret
        // If you are testing with the CLI, find the secret by running 'stripe listen'
        // If you are using an endpoint defined with the API or dashboard, look in your webhook settings
        // at https://dashboard.stripe.com/webhooks

        Event event;

        if(payload != null) {
            // Only verify the event if you have an endpoint secret defined.
            // Otherwise use the basic event deserialized with GSON.
            try {
                event = ApiResource.GSON.fromJson(payload,Event.class);
            } catch (JsonSyntaxException e) {
                // Invalid signature
                logger.info("⚠️  Webhook error while validating signature."+e);
                return "";
            }
        }else {
            return "";
        }
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                if(paymentIntent != null){
                    logger.info("Payment for "+paymentIntent.getAmount()/100+"  succeeded.");
                    handleSuccessPayments(paymentIntent);
                }
                break;
            default:
        }

        return "";


    }

    private void handleSuccessPayments(PaymentIntent paymentIntent) {
        String reservationIds = paymentIntent.getMetadata().get("reservation_ids");
        String[] reservationIdArray = reservationIds.split(",");
        List<Integer> reservationIdList = new ArrayList<>();

        for(String revId : reservationIdArray){
            reservationIdList.add(Integer.parseInt(revId));
        }

        List<Reservation> reservationList = reservationService.findReservationsByRevIdList(reservationIdList);
        List<Reservation> toBeUpdateReservations = new ArrayList<>();
        List<SeatReservation> toBeUpdateSeatReservations = new ArrayList<>();

        Payment payment = new Payment();
        payment.setSuccess(true);
        payment.setActive(true);
        payment.setAmount(paymentIntent.getAmount()/100);
        payment.setPaymentIntentId(paymentIntent.getId());

        Payment savedPayment = paymentService.savePayment(payment);

        for(Reservation r : reservationList){
            r.setPayment(savedPayment);
            r.setPaymentCompleted(true);

            toBeUpdateReservations.add(r);

            List<SeatReservation> seatReservationList = reservationService.findReservedSeatsByRevId(r.getReservationId());

            for(SeatReservation sr : seatReservationList){
                sr.setStatus("Success");

                toBeUpdateSeatReservations.add(sr);
            }
        }

        reservationService.updateReservations(toBeUpdateReservations);
        reservationService.updateSeatReservations(toBeUpdateSeatReservations);

        int userId = Integer.parseInt(paymentIntent.getMetadata().get("userId"));
        deleteCartData(userId);

    }

    private void deleteCartData(int userId) {
        reservationService.deleteCartDataByUserId(userId);
    }

}
