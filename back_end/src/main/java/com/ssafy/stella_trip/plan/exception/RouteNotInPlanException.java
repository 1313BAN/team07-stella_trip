package com.ssafy.stella_trip.plan.exception;

public class RouteNotInPlanException extends RuntimeException {
    public RouteNotInPlanException(String message) {
        super(message);
    }
}
