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
        log.warn("ATTRACTION-002> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-002", "해당 리뷰의 id와 여행지 id가 일치하지 않습니다."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReviewWriterNotMatchToUser.class)
    public CommonResponse<ErrorBody> ReviewWriterNotMatchToUser(ReviewWriterNotMatchToUser e, HttpServletRequest request) {
        log.warn("ATTRACTION-003> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-003", "해당 리뷰에 대한 권한이 없습니다."),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ReviewAlreadyLikedException.class)
    public CommonResponse<ErrorBody> handleReviewAlreadyLikedException(
            ReviewAlreadyLikedException e, HttpServletRequest request) {
        log.warn("ATTRACTION-004> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-004", "이미 좋아요를 누른 리뷰입니다."),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReviewNotLikedException.class)
    public CommonResponse<ErrorBody> handleReviewNotLikedException(
            ReviewNotLikedException e, HttpServletRequest request) {
        log.warn("ATTRACTION-005> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-005", "좋아요를 누르지 않은 리뷰입니다."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AttractionNotFoundException.class)
    public CommonResponse<ErrorBody> AttractionNotFoundException(
            AttractionNotFoundException e, HttpServletRequest request) {
        log.warn("ATTRACTION-006> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-006", "해당 id의 여행지를 찾을 수 없습니다."),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AttractionAlreadyLikedException.class)
    public CommonResponse<ErrorBody> AttractionAlreadyLikedException(
            AttractionAlreadyLikedException e, HttpServletRequest request) {
        log.warn("ATTRACTION-007> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-007", "이미 좋아요를 누른 여행지입니다."),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AttractionNotLikedException.class)
    public CommonResponse<ErrorBody> AttractionNotLikedException(
            AttractionNotLikedException e, HttpServletRequest request) {
        log.warn("ATTRACTION-008> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("ATTRACTION-008", "좋아요를 누르지 않은 여행지입니다."),
                HttpStatus.BAD_REQUEST);
    }
}