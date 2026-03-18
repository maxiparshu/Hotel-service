package by.beltam.practice.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    @Bean
    public FeignErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
