package com.supernova.fashionnova.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import static org.springframework.http.HttpStatus.OK;

@Getter
public class CommonResponse<T> {

    private final int statusCode; // 200
    private final String reasonPhrase;  // OK
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    @Builder
    protected CommonResponse(int statusCode, String reasonPhrase, String message, T data) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResponse<T> of(T data) {
        return new CommonResponse<>(OK.value(), OK.name(), "성공", data);
    }

    public static <T> CommonResponse<T> of(String message) {
        return new CommonResponse<>(OK.value(), OK.name(), message, null);
    }

    public static <T> CommonResponse<T> of(String message, T data) {
        return new CommonResponse<>(OK.value(), OK.name(), message, data);
    }

}
