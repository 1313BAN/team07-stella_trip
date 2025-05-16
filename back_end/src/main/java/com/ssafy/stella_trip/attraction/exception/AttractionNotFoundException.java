package com.ssafy.stella_trip.attraction.exception;

public class AttractionNotFoundException extends RuntimeException {
    public AttractionNotFoundException(String message) {
        super(message);
    }
}
