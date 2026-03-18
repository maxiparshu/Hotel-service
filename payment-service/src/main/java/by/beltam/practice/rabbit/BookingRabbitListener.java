package by.beltam.practice.rabbit;

import by.beltam.practice.common.dto.payment.PaymentRequestEvent;
import by.beltam.practice.config.RabbitConfig;
import by.beltam.practice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingRabbitListener {

    private final PaymentService paymentService;

    @RabbitListener(queues = RabbitConfig.BOOKING_REQUESTS_QUEUE)
    public void handlePaymentRequest(PaymentRequestEvent event) {
        log.info("Message received: payment request for booking [{}]", event.bookingId());

        try {
            paymentService.processPayment(event);
            log.info("Successfully processed payment for booking [{}]", event.bookingId());
        } catch (Exception e) {
            log.error("Error processing payment for booking [{}]: {}", event.bookingId(), e.getMessage());
        }
    }
}