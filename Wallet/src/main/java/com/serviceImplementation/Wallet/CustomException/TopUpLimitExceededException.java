package com.serviceImplementation.Wallet.CustomException;

public class TopUpLimitExceededException extends RuntimeException{
    public TopUpLimitExceededException(String message){
        super(message);
    }
}
