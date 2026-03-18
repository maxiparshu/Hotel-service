package by.beltam.practice.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseBusinessException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT, ErrorCode.CONFLICT);
    }
}