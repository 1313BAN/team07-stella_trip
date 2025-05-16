package com.ssafy.stella_trip.attraction.exception;

public class ReviewWriterNotMatchToUser extends RuntimeException {
    public ReviewWriterNotMatchToUser(String message) {
        super(message);
    }
}
