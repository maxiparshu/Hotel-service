package by.beltam.practice.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseBusinessException {

    public NotFoundException(String message) {
        super(message + "not found", HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND);
    }
}