package com.ssafy.stella_trip.user.exception;

public class SignupFailureException extends RuntimeException {
    public SignupFailureException(String message) {
        super(message);
    }
}
