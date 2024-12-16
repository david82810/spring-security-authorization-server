package com.david.adapter.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    public String message;
    private T data;
    private boolean status;
    private Integer statusCode;

    public static <T> Result<T> success(T data) {
        return new Result<>("Success", data, Boolean.TRUE, HttpStatus.OK.value());
    }

    public static <T> Result<T> success() {
        return new Result<>("Success", null, Boolean.TRUE, HttpStatus.OK.value());
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(message, data, Boolean.TRUE, HttpStatus.OK.value());
    }

    public static <T>Result<T> error(HttpStatus status, String message) {
        return new Result<>(message, null, Boolean.FALSE, status.value());
    }



    public static <T>Result<T> error(HttpStatus status, String message, T data) {
        return new Result<>(message, data, Boolean.FALSE, status.value());
    }




}
