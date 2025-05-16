package com.ssafy.stella_trip.attraction.exception;

public class ReviewAlreadyLikedException extends RuntimeException {
    public ReviewAlreadyLikedException(String message) {
        super(message);
    }
}