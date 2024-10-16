package com.project.seatReservation.service;

import com.project.seatReservation.model.Payment;
import com.project.seatReservation.model.Wallet;
import com.project.seatReservation.response.PaymentResponse;
import com.stripe.exception.StripeException;

import java.util.List;

public interface PaymentService {
    public PaymentResponse createPaymentLink(int userId, double amount, List<Integer> reservationIdList) throws StripeException;

    Payment savePayment(Payment payment);

    void updatePayment(Payment toBeUpdatePayment);

    Wallet findWalletByPassengerId(int passengerId);

    void updateWallet(Wallet wallet);

    void saveWallet(Wallet newWallet);
}
