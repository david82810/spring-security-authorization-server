package com.david.adapter.advice;

import com.david.adapter.controller.response.Result;
import com.david.exception.CustomerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Result.error(HttpStatus.METHOD_NOT_ALLOWED,e.getLocalizedMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Result.error(HttpStatus.BAD_REQUEST,e.getLocalizedMessage());
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return Result.error(HttpStatus.BAD_REQUEST,"請求參數有誤",fieldErrors.stream().map(FieldError::getDefaultMessage).toList());
    }

    @ExceptionHandler(value = CustomerException.class)
    public Result<String> handleException(CustomerException e) {
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, Objects.isNull(e.getMessage()) ? "系統錯誤" : e.getMessage());
    }

}
