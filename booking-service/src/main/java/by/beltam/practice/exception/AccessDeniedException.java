package by.beltam.practice.exception;

import by.beltam.practice.common.exception.BaseBusinessException;
import by.beltam.practice.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BaseBusinessException {
    public AccessDeniedException(String message) {

        super(
                message,
                HttpStatus.FORBIDDEN,
                ErrorCode.FORBIDDEN);
    }

}
