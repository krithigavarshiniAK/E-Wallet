package com.serviceImplementation.Wallet.CustomException;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }

}
