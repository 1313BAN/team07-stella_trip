package com.ssafy.stella_trip.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * ErrorCode를 반환하기 위한 Body DTO
 */
@AllArgsConstructor
@Getter
public class ErrorBody {
    String code;
    String message;
}
