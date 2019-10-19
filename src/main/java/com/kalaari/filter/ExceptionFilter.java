package com.kalaari.filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kalaari.exception.KalaariException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionFilter {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex) throws Exception {
        if (ex instanceof KalaariException) {
            HttpStatus httpStatus = ((KalaariException) ex).getCode().getHttpStatus();
            String errorResponse = ex.getMessage();
            return ResponseEntity.status(httpStatus).body(errorResponse);
        } else {
            throw ex;
        }
    }

}
