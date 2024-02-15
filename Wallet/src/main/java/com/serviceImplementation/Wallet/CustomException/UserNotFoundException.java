package com.serviceImplementation.Wallet.CustomException;


public class UserNotFoundException extends RuntimeException
{

    public static String getMessage;

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);

    }
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
