package com.ssafy.stella_trip.plan.exception;

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
public class PlanExceptionHandler {

    @ExceptionHandler(PlanNotFoundException.class)
    public CommonResponse<ErrorBody> planNotFoundException(PlanNotFoundException e, HttpServletRequest request) {
        log.warn("PLAN-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("PLAN-001", "해당 ID의 계획을 찾을 수 없습니다."),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedLikeException.class)
    public CommonResponse<ErrorBody> duplicatedLikeException(DuplicatedLikeException e, HttpServletRequest request) {
        log.warn("PLAN-002> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("PLAN-002", "중복된 좋아요 등록 / 취소 입니다."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedPlanAccessException.class)
    public CommonResponse<ErrorBody> unauthorizedPlanAccessException(UnauthorizedPlanAccessException e, HttpServletRequest request) {
        log.warn("PLAN-003> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("PLAN-003", "해당 계획에 대한 접근 권한이 없습니다."),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LockedPlanException.class)
    public CommonResponse<ErrorBody> lockedPlanException(LockedPlanException e, HttpServletRequest request) {
        log.warn("PLAN-004> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("PLAN-004", "해당 계획은 누군가 수정중입니다."),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalDayIndexException.class)
    public CommonResponse<ErrorBody> illegalDayIndexException(IllegalDayIndexException e, HttpServletRequest request) {
        log.warn("PLAN-005> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("PLAN-005", "잘못된 여행 날짜입니다."),
                HttpStatus.BAD_REQUEST);
    }
}
