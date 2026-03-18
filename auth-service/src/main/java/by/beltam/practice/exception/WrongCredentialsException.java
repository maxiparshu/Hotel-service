package by.beltam.practice.exception;

import by.beltam.practice.common.exception.BaseBusinessException;
import by.beltam.practice.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class WrongCredentialsException extends BaseBusinessException {
   public WrongCredentialsException() {
       super(
               "wrong credentials",
               HttpStatus.FORBIDDEN,
               ErrorCode.FORBIDDEN);
   }
}
