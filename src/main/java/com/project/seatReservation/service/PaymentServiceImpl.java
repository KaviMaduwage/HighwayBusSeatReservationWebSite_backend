package com.project.seatReservation.service;

import com.project.seatReservation.dao.PaymentDao;
import com.project.seatReservation.dao.WalletDao;
import com.project.seatReservation.model.Payment;
import com.project.seatReservation.model.Reservation;
import com.project.seatReservation.model.Wallet;
import com.project.seatReservation.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService{
    PaymentDao paymentDao;
    WalletDao walletDao;

    public PaymentServiceImpl(PaymentDao paymentDao,WalletDao walletDao) {
        this.paymentDao = paymentDao;
        this.walletDao = walletDao;
    }

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Override
    public PaymentResponse createPaymentLink(int userId, double amount, List<Integer> reservationIdList) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        String revIds = "";
        for(int revId : reservationIdList){
            revIds += revId+",";
        }

        revIds = revIds.substring(0,revIds.length()-1);


        SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(
                SessionCreateParams.
                        PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment/success")
                .setCancelUrl("http://localhost:3000/payment/fail")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("lkr")
                                .setUnitAmount((long) amount*100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("My Seat").build())
                                .build()
                        )
                        .build()
                )
                .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("reservation_ids", revIds)
                        .putMetadata("userId", String.valueOf(userId))
                        .build())


                .build();


        Session session = Session.create(params);

        PaymentResponse response = new PaymentResponse();

//        PaymentIntent intent = PaymentIntent.retrieve(session.getPaymentIntent());
//        String paymentStatus = intent.getStatus();
//        System.out.println(paymentStatus);

        response.setPayment_url(session.getUrl());
        response.setPayment_status(session.getPaymentStatus());
        return response;
    }

    @Override
    public Payment savePayment(Payment payment) {
        return paymentDao.save(payment);
    }

    @Transactional
    @Override
    public void updatePayment(Payment toBeUpdatePayment) {
        paymentDao.save(toBeUpdatePayment);
    }

    @Override
    public Wallet findWalletByPassengerId(int passengerId) {
        return walletDao.findWalletByPassengerId(passengerId);
    }

    @Transactional
    @Override
    public void updateWallet(Wallet wallet) {
        walletDao.save(wallet);
    }

    @Override
    public void saveWallet(Wallet newWallet) {
        walletDao.save(newWallet);
    }
}
