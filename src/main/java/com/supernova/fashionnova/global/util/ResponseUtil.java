package com.supernova.fashionnova.global.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ResponseUtil {

    public static <T> ResponseEntity<T> of(HttpStatus httpStatus, T data) {
        return  ResponseEntity.status(httpStatus).body(data);
    }

    public static ResponseEntity<String> of(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(message);
    }

}
