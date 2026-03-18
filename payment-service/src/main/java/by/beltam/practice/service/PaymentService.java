package by.beltam.practice.service;


import by.beltam.practice.common.dto.payment.PaymentRequestEvent;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    void processPayment(PaymentRequestEvent request);
}
