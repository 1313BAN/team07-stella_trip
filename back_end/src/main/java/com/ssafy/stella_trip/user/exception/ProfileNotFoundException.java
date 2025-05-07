package com.ssafy.stella_trip.user.exception;

/**
 * 프로필 조회 실패 Exception
 */
public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(String message) {
        super(message);
    }
}
