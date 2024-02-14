package com.serviceImplementation.Wallet.CustomException;


public class ResourceNotFoundException extends RuntimeException
{

    public static String getMessage;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);

    }
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
