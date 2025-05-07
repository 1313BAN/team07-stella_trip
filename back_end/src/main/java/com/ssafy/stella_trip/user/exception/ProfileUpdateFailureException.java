package com.ssafy.stella_trip.user.exception;

/**
 * 프로필 업데이트 실패 Exception
 */
public class ProfileUpdateFailureException extends RuntimeException {
    public ProfileUpdateFailureException(String message) {
        super(message);
    }
}
