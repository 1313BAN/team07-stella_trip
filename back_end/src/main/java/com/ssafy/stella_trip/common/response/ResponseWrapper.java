package com.ssafy.stella_trip.common.response;

public class ResponseWrapper<T> {
    int code;
    T body;

    ResponseWrapper(int code, T body) {
        this.code = code;
        this.body = body;
    }
}
