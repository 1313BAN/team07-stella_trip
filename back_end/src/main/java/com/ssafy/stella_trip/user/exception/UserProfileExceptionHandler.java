package com.ssafy.stella_trip.user.exception;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.common.response.ErrorBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserProfileExceptionHandler {

    // 프로필 조회 실패
    @ExceptionHandler(ProfileNotFoundException.class)
    public CommonResponse<ErrorBody> profileNotFoundException(ProfileNotFoundException e, HttpServletRequest request) {
        log.warn("PROFILE-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("PROFILE-001", "프로필을 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
    }

    // 프로필 업데이트 실패
    @ExceptionHandler(ProfileUpdateFailureException.class)
    public CommonResponse<ErrorBody> profileUpdateFailureException(ProfileUpdateFailureException e, HttpServletRequest request) {
        log.warn("PROFILE-002> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("PROFILE-002", "프로필 업데이트에 실패했습니다."), HttpStatus.BAD_REQUEST);
    }

    // 비밀번호 업데이트 실패
    @ExceptionHandler(PasswordUpdateFailureException.class)
    public CommonResponse<ErrorBody> passwordUpdateFailureException(PasswordUpdateFailureException e, HttpServletRequest request) {
        log.warn("PROFILE-003> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("PROFILE-003", "비밀번호 업데이트에 실패했습니다."), HttpStatus.BAD_REQUEST);
    }
}