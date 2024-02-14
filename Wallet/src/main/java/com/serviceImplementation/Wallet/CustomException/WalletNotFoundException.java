package com.serviceImplementation.Wallet.CustomException;

public class WalletNotFoundException extends RuntimeException{
    public static String getMessage;


    public WalletNotFoundException(String message) {
        super(message);

    }
    public WalletNotFoundException(Throwable cause) {
        super(cause);
    }
    public WalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
