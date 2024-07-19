package com.supernova.fashionnova.global.exception;

import lombok.Getter;

@Getter
public class ExceptionDto {

    private final String result;
    private ErrorType errorType;
    private final String message;

    public ExceptionDto(ErrorType errorType) {
        this.result = "ERROR";
        this.errorType = errorType;
        this.message = errorType.getMessage();
    }

    public ExceptionDto(String message) {
        this.result = "ERROR";
        this.message = message;
    }

}
