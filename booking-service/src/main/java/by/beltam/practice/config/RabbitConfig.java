package by.beltam.practice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String BOOKING_EXCHANGE = "booking-exchange";
    public static final String BOOKING_ROUTING_KEY = "booking.request";
    public static final String BOOKING_REQUESTS_QUEUE = "booking-requests";
    public static final String PAYMENT_RESULTS_QUEUE = "payment-results";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(BOOKING_EXCHANGE, true, false);
    }

    @Bean
    public Queue bookingRequestsQueue() {
        return new Queue(BOOKING_REQUESTS_QUEUE, true);
    }

    @Bean
    public Binding bookingBinding() {
        return BindingBuilder.bind(bookingRequestsQueue())
                .to(bookingExchange())
                .with(BOOKING_ROUTING_KEY);
    }

    @Bean
    public Queue paymentResultsQueue() {
        return new Queue(PAYMENT_RESULTS_QUEUE, true);
    }
}