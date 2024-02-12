package com.serviceImplementation.Wallet.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {WalletNotFoundException.class})
    public ResponseEntity<Object> handleWalletNotFoundException
            (WalletNotFoundException walletNotFoundException)
    {
        ErrorResponse errorResponse = new ErrorResponse(
                walletNotFoundException.getMessage(),
                walletNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(walletNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InsufficientBalanceException.class})
    public ResponseEntity<Object> handleInsufficientBalanceException
            (InsufficientBalanceException insufficientBalanceException)
    {
        ErrorResponse errorResponse = new ErrorResponse(
                insufficientBalanceException.getMessage(),
                insufficientBalanceException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(insufficientBalanceException, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = {TopUpLimitExceededException.class})
    public ResponseEntity<Object> handleTopUpLimitExceededException
            (TopUpLimitExceededException topUpLimitExceededExceptionn)
    {
        ErrorResponse errorResponse = new ErrorResponse(
                topUpLimitExceededExceptionn.getMessage(),
                topUpLimitExceededExceptionn.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(topUpLimitExceededExceptionn, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {TransactionNotFoundException.class})
    public ResponseEntity<Object> handleTransactionNotFoundException
            (TransactionNotFoundException transactionNotFoundException)
    {
        ErrorResponse errorResponse = new ErrorResponse(
                transactionNotFoundException.getMessage(),
                transactionNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(transactionNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException
            (ResourceNotFoundException resourceNotFoundException)
    {
        ErrorResponse errorResponse = new ErrorResponse(
                resourceNotFoundException.getMessage(),
                resourceNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(resourceNotFoundException, HttpStatus.NOT_FOUND);
    }
}




