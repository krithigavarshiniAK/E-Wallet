package com.serviceImplementation.Wallet.CustomException;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String description;

    public ErrorResponse(LocalDateTime timestamp, String message, String details) {
        super();
        this.timestamp = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.message = message;
        this.description = details;
    }


    @Override
    public String toString() {
        return "MyErrorDetails [timestamp=" + timestamp + ", message=" + message + ", details=" + description + "]";
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ErrorResponse() {
    }

}
