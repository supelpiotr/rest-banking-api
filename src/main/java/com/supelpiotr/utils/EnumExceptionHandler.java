package com.supelpiotr.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class EnumExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO enumValidationError(MethodArgumentTypeMismatchException ex) {
        return new ErrorDTO(ErrorConstants.ERR_CONVERSION_NOT_SUPPORTED, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO currencyValidationError(HttpMessageNotReadableException ex) {
        return new ErrorDTO(ErrorConstants.ERR_MESSAGE_NOT_READABLE, ex.getMessage());
    }

}