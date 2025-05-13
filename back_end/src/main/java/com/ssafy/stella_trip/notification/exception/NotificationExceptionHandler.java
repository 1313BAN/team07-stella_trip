package com.ssafy.stella_trip.notification.exception;

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
public class NotificationExceptionHandler {
    @ExceptionHandler(NotificationNotFoundException.class)
    public CommonResponse<ErrorBody> notificationNotFoundException(NotificationNotFoundException e, HttpServletRequest request) {
        log.warn("COMMON-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("NOTICE-01", "해당"),
                HttpStatus.NOT_FOUND);
    }
}
