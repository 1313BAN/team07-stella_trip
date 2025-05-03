package com.ssafy.stella_trip.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWrapper<T> {
    int code;
    T body;

    ResponseWrapper(int code, T body) {
        this.code = code;
        this.body = body;
    }
}
