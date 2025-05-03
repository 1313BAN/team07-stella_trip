package com.ssafy.stella_trip.common.exception;

import com.ssafy.stella_trip.common.response.CommonResponse;
import com.ssafy.stella_trip.common.response.ErrorBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 오류 handling을 위한 advice
 */

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(Exception.class)
    public CommonResponse<ErrorBody> internalServerException(Exception e, HttpServletRequest request) {
        log.warn("COMMON-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("COMMON-001", "서버 내부에서 오류가 발생하였습니다."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
