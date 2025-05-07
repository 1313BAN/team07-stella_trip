package com.ssafy.stella_trip.user.exception;

/**
 * 비밀번호 업데이트 실패 Exception
 */
public class PasswordUpdateFailureException extends RuntimeException {
    public PasswordUpdateFailureException(String message) {
        super(message);
    }
}