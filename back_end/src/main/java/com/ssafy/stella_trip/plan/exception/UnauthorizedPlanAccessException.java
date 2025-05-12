package com.ssafy.stella_trip.plan.exception;

public class UnauthorizedPlanAccessException extends RuntimeException {
    public UnauthorizedPlanAccessException(String message) {
        super(message);
    }
}
