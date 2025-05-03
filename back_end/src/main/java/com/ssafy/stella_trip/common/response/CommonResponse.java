package com.ssafy.stella_trip.common.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class CommonResponse<T> extends ResponseEntity<ResponseWrapper<T>> {
    public CommonResponse(T body, HttpStatusCode status) {
        super(new ResponseWrapper<T>(status.value(), body), status);
    }

    public CommonResponse(T body, HttpStatusCode status, HttpHeaders headers) {
        super(new ResponseWrapper<T>(status.value(), body), headers, status);
    }

    public static <T> CommonResponseBuilder<T> builder() {
        return new CommonResponseBuilder<>();
    }

    public static class CommonResponseBuilder<T> {
        private HttpStatusCode status = HttpStatus.OK;
        private HttpHeaders headers = new HttpHeaders();
        private T body = null;

        public CommonResponseBuilder<T> status(HttpStatusCode status) {
            this.status = status;
            return this;
        }

        public CommonResponseBuilder<T> header(String key, String value) {
            this.headers.add(key, value);
            return this;
        }

        public CommonResponseBuilder<T> body(T body) {
            this.body = body;
            return this;
        }

        public CommonResponse<T> build() {
            return new CommonResponse<>(body, status, headers);
        }
    }
}
