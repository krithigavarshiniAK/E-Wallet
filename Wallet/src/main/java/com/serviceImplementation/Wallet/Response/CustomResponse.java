package com.serviceImplementation.Wallet.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse <T>{
    private int statusCode;
    private String description;
    private T data;
    private String error;

    public CustomResponse(int statusCode, String description, T data) {
        this.statusCode = statusCode;
        this.description = description;
        this.data = data;
    }

    public CustomResponse(int statusCode, String error) {
        this.statusCode = statusCode;
        this.error = error;
    }

}
