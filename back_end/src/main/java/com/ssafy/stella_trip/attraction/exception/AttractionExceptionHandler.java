package com.ssafy.stella_trip.attraction.exception;

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
public class AttractionExceptionHandler {
    @ExceptionHandler(ReviewNotFoundException.class)
    public CommonResponse<ErrorBody> ReviewNotFoundException(ReviewNotFoundException e, HttpServletRequest request) {
        log.warn("ATTRACTION-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-001", "해당 ID의 리뷰를 찾을 수 없습니다."),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewNotMatchToAttractionException.class)
    public CommonResponse<ErrorBody> ReviewNotMatchToAttractionException(ReviewNotMatchToAttractionException e, HttpServletRequest request) {
        log.warn("ATTRACTION-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-001", "해당 ID의 리뷰를 찾을 수 없습니다."),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewWriterNotMatchToUser.class)
    public CommonResponse<ErrorBody> ReviewWriterNotMatchToUser(ReviewWriterNotMatchToUser e, HttpServletRequest request) {
        log.warn("ATTRACTION-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-001", "해당 ID의 리뷰를 찾을 수 없습니다."),
                HttpStatus.NOT_FOUND);
    }
}
