package com.serviceImplementation.Wallet.CustomException;

public class ErrorResponse {
        private String message;


        public ErrorResponse(String message) {
            this.message = message;
        }

        // Getters and setters (You may need to generate them depending on your IDE)

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }


