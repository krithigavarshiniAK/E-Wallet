package com.serviceImplementation.Wallet.CustomException;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<Object> exception(WalletNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Object> exception(InsufficientBalanceException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> exception(ResourceNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TopUpLimitExceededException.class)
    public ResponseEntity<Object> exception(TopUpLimitExceededException exception) {

        return new ResponseEntity<>(new ErrorResponse(exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Object> exception(TransactionNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }


}


