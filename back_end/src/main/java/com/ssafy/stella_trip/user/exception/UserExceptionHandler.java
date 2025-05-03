package com.ssafy.stella_trip.user.exception;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.common.response.ErrorBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public CommonResponse<ErrorBody> invalidUserException(BadCredentialsException e, HttpServletRequest request) {
        log.warn("USER-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-001", "잘못된 사용자입니다."), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public CommonResponse<ErrorBody> invalidUserException(UsernameNotFoundException e, HttpServletRequest request) {
        log.warn("USER-002> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-002", "잘못된 사용자입니다."), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public CommonResponse<ErrorBody> invalidUserException(DuplicateKeyException e, HttpServletRequest request) {
        log.warn("USER-003> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-003", "중복된 이메일이 있습니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignupFailureException.class)
    public CommonResponse<ErrorBody> invalidUserException(SignupFailureException e, HttpServletRequest request) {
        log.warn("USER-004> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-004", "회원가입에 실패하였습니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public CommonResponse<ErrorBody> invalidUserException(InternalAuthenticationServiceException e, HttpServletRequest request) {
        log.warn("USER-005> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-005", "로그인에 실패하였습니다."), HttpStatus.BAD_REQUEST);
    }
}
