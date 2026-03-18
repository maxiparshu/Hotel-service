package by.beltam.practice.service.impl;

import by.beltam.practice.common.dto.payment.PaymentRequestEvent;
import by.beltam.practice.common.dto.payment.PaymentResultEvent;
import by.beltam.practice.config.RabbitConfig;
import by.beltam.practice.entity.Payment;
import by.beltam.practice.repository.PaymentRepository;
import by.beltam.practice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public void processPayment(PaymentRequestEvent request) {
        log.info("Processing payment for booking: [{}]", request.bookingId());

        boolean isSuccess = Math.random() > 0.1;
        String txId = UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .bookingId(request.bookingId())
                .userId(request.userId())
                .cost(request.cost())
                .status(isSuccess ? "SUCCESS" : "FAILED")
                .transactionId(txId)
                .build();

        paymentRepository.save(payment);

        PaymentResultEvent result = new PaymentResultEvent(
                request.bookingId(),
                isSuccess,
                txId,
                isSuccess ? "Payment processed successfully" : "Insufficient funds"
        );

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                rabbitTemplate.convertAndSend(
                        RabbitConfig.PAYMENT_RESULTS_EXCHANGE,
                        RabbitConfig.PAYMENT_ROUTING_KEY,
                        result
                );
            }
        });

        log.info("Payment result sent for booking [{}]. Status: {}", request.bookingId(), isSuccess);
    }
}