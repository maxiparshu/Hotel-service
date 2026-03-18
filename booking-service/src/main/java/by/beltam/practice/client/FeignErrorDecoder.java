package by.beltam.practice.client;

import by.beltam.practice.common.exception.BadRequestException;
import by.beltam.practice.common.exception.ConflictException;
import by.beltam.practice.common.exception.ErrorResponse;
import by.beltam.practice.common.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.InputStream;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.body() == null) {
            return defaultDecoder.decode(methodKey, response);
        }

        try (InputStream body = response.body().asInputStream()) {

            ErrorResponse error = mapper.readValue(body, ErrorResponse.class);

            String code = error.error() != null ? error.error().toUpperCase() : "";

            return switch (code) {
                case "NOT_FOUND" -> new NotFoundException(error.message());
                case "BAD_REQUEST" -> new BadRequestException(error.message());
                case "CONFLICT" -> new ConflictException(error.message());
                default -> new RuntimeException(error.message());
            };

        } catch (Exception e) {
            return defaultDecoder.decode(methodKey, response);
        }
    }
}