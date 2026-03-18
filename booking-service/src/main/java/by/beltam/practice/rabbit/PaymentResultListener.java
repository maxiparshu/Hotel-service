package by.beltam.practice.rabbit;

import by.beltam.practice.common.dto.payment.PaymentResultEvent;
import by.beltam.practice.config.RabbitConfig;
import by.beltam.practice.service.BookingService;
import by.beltam.practice.utility.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResultListener {

    private final BookingService bookingService;

    @RabbitListener(queues = RabbitConfig.PAYMENT_RESULTS_QUEUE)
    public void handlePaymentResult(PaymentResultEvent event) {
        log.info("Message received: payment result for booking [{}] - Success: {}",
                event.bookingId(), event.success());

        BookingStatus targetStatus = event.success() ? BookingStatus.CONFIRMED : BookingStatus.REJECTED;

        try {
            bookingService.updateStatus(event.bookingId(), targetStatus, null, "SYSTEM");
            log.info("Booking [{}] status updated to {}", event.bookingId(), targetStatus);
        } catch (Exception e) {
            log.error("Failed to update status for booking [{}]: {}", event.bookingId(), e.getMessage());
        }
    }
}