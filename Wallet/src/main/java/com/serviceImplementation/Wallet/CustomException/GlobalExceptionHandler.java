package com.serviceImplementation.Wallet.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {WalletNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleWalletNotFoundException
            (WalletNotFoundException walletNotFoundException,WebRequest req)
    {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(walletNotFoundException.getMessage());
        errorResponse.setDescription(req.getDescription(false));

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException
            (ResourceNotFoundException resourceNotFoundException,WebRequest req)
    { ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(resourceNotFoundException.getMessage());
        errorResponse.setDescription(req.getDescription(false));

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {TopUpLimitExceededException.class})
    public ResponseEntity<ErrorResponse> handleTopUpLimitExceededException
            (TopUpLimitExceededException topUpLimitExceededException,WebRequest req)
    { ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(topUpLimitExceededException.getMessage());
        errorResponse.setDescription(req.getDescription(false));

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TransactionNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleTransactionNotFoundException
            (TransactionNotFoundException transactionNotFoundException,WebRequest req)
    { ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(transactionNotFoundException.getMessage());
        errorResponse.setDescription(req.getDescription(false));

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {InsufficientBalanceException.class})
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException
            (InsufficientBalanceException insufficientBalanceException,WebRequest req)
    { ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(insufficientBalanceException.getMessage());
        errorResponse.setDescription(req.getDescription(false));

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException
            (IllegalArgumentException illegalArgumentException,WebRequest req)
    { ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(illegalArgumentException.getMessage());
        errorResponse.setDescription(req.getDescription(false));

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public ResponseEntity<ErrorResponse> handle
            (UserAlreadyExistException userAlreadyExistException,WebRequest req)
    { ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(userAlreadyExistException.getMessage());
        errorResponse.setDescription(req.getDescription(false));

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}




