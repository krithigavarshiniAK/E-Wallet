package com.serviceImplementation.Wallet.CustomException;

public class WalletNotFoundException extends RuntimeException{
    public Double WalletNotFoundException;

    public WalletNotFoundException(String message){
        super(message);
    }

    public WalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
