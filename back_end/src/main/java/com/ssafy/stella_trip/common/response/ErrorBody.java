package com.ssafy.stella_trip.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorBody {
    String code;
    String message;
}
