package com.ssafy.stella_trip.user.exception;

/**
 * 회원가입 실패 Exception, 모종의 이유로 등록이 안됨
 */
public class SignupFailureException extends RuntimeException {
    public SignupFailureException(String message) {
        super(message);
    }
}
