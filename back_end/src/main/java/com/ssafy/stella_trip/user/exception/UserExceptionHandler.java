package com.ssafy.stella_trip.user.exception;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.common.response.ErrorBody;
import com.ssafy.stella_trip.security.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {
    // 인증정보가 틀렸을 때
    @ExceptionHandler(BadCredentialsException.class)
    public CommonResponse<ErrorBody> invalidUserException(BadCredentialsException e, HttpServletRequest request) {
        log.warn("USER-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-001", "잘못된 사용자입니다."), HttpStatus.UNAUTHORIZED);
    }

    // 해당 이메일의 계정이 없음
    @ExceptionHandler(UsernameNotFoundException.class)
    public CommonResponse<ErrorBody> userNameNotFoundException(UsernameNotFoundException e, HttpServletRequest request) {
        log.warn("USER-002> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-002", "잘못된 사용자입니다."), HttpStatus.UNAUTHORIZED);
    }

    // 중복 이메일로 회원가입시
    @ExceptionHandler(DuplicateKeyException.class)
    public CommonResponse<ErrorBody> duplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        log.warn("USER-003> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-003", "중복된 이메일이 있습니다."), HttpStatus.BAD_REQUEST);
    }

    // 회원가입 실패
    @ExceptionHandler(SignupFailureException.class)
    public CommonResponse<ErrorBody> signupFailureException(SignupFailureException e, HttpServletRequest request) {
        log.warn("USER-004> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-004", "회원가입에 실패하였습니다."), HttpStatus.BAD_REQUEST);
    }

    // 보통 비번 틀릴때 exception
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public CommonResponse<ErrorBody> internalAuthenticationServiceException(InternalAuthenticationServiceException e, HttpServletRequest request) {
        log.warn("USER-005> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-005", "로그인에 실패하였습니다."), HttpStatus.BAD_REQUEST);
    }

    // refresh token이 문제가 있을때
    @ExceptionHandler(InvalidTokenException.class)
    public CommonResponse<ErrorBody> invalidTokenException(InvalidTokenException e, HttpServletRequest request) {
        log.warn("USER-006> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-006", "토큰이 잘못되었습니다."), HttpStatus.BAD_REQUEST);
    }
}
