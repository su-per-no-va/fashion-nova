package com.supernova.fashionnova.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleScheduleException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorType().getHttpStatus()).body(new ExceptionDto(ex.getErrorType()));
    }

//    밸리데이션 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder builder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage()).append("\n");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.toString());
    }



}
