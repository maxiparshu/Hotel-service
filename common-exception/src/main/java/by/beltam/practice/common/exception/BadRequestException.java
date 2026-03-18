package by.beltam.practice.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseBusinessException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST);
    }
}