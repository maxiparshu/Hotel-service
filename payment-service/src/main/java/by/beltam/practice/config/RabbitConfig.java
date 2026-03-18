package by.beltam.practice.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String BOOKING_REQUESTS_QUEUE = "booking-requests";
    public static final String PAYMENT_RESULTS_EXCHANGE = "payment-results-exchange";
    public static final String PAYMENT_ROUTING_KEY = "payment.result.key";
    public static final String PAYMENT_RESULTS_QUEUE = "payment-results";
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue bookingRequestsQueue() {
        return new Queue(BOOKING_REQUESTS_QUEUE, true);
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(PAYMENT_RESULTS_EXCHANGE, true, false);
    }

    @Bean
    public Queue paymentResultsQueue() {
        return new Queue(PAYMENT_RESULTS_QUEUE, true);
    }

    @Bean
    public Binding paymentBinding() {
        return BindingBuilder.bind(paymentResultsQueue())
                .to(paymentExchange())
                .with(PAYMENT_ROUTING_KEY);
    }
}