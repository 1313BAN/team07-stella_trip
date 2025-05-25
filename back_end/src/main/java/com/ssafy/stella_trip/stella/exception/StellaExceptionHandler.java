package com.ssafy.stella_trip.stella.exception;

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
@Order(Ordered.LOWEST_PRECEDENCE)
public class StellaExceptionHandler {
    @ExceptionHandler(StellaNotFoundException.class)
    public CommonResponse<ErrorBody> handleStellaNotFoundException(StellaNotFoundException e, HttpServletRequest request) {
        log.warn("STELLA-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("STELLA-001", "해당 별자리를 찾지 못했습니다."), HttpStatus.NOT_FOUND);
    }

}
