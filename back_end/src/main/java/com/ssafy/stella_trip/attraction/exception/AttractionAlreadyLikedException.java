package com.ssafy.stella_trip.attraction.exception;

public class AttractionAlreadyLikedException extends RuntimeException {
    public AttractionAlreadyLikedException(String message) {
        super(message);
    }
}
