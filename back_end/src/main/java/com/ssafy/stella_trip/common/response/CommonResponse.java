package com.ssafy.stella_trip.common.response;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class CommonResponse<T> extends ResponseEntity<ResponseWrapper<T>> {
    public CommonResponse(T body, HttpStatusCode status) {
        super(new ResponseWrapper<T>(status.value(), body), status);
    }
}
