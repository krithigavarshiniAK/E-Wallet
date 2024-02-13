package com.serviceImplementation.Wallet.CustomException;

public class TransactionNotFoundException extends Throwable {

    public TransactionNotFoundException(String message) {
        super(message);
    }


    public TransactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
