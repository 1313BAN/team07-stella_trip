package com.ssafy.stella_trip.plan.exception;

public class LockedPlanException extends RuntimeException {
    public LockedPlanException(String message) {
        super(message);
    }
}
