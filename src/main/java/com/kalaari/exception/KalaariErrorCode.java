package com.kalaari.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum KalaariErrorCode {
    NOT_FOUND("NOT_FOUND", HttpStatus.NOT_FOUND),
    BAD_REQUEST("BAD_REQUEST", HttpStatus.BAD_REQUEST),
    UNKNOWN("UNKNOWN"),
    METHOD_ARGUMENT_NOT_VALID("UNPROCESSABLE_ENTITY", HttpStatus.UNPROCESSABLE_ENTITY),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    DOWNSTREAM_ERROR("DOWNSTREAM_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    DB_ERROR("DB_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    RESPONSE_NULL("RESPONSE_NULL", HttpStatus.INTERNAL_SERVER_ERROR),
    UNPROCESSABLE_ENTITY("UNPROCESSABLE_ENTITY", HttpStatus.UNPROCESSABLE_ENTITY);

    @Getter
    private final String name;

    @Getter
    private Integer errorCode;

    @Getter
    private HttpStatus httpStatus;

    KalaariErrorCode(String name, Integer errorCode) {
        this.name = name;
        this.errorCode = errorCode;
    }

    KalaariErrorCode(String name, HttpStatus httpStatus) {
        this.name = name;
        this.httpStatus = httpStatus;
    }

    KalaariErrorCode(String name, HttpStatus httpStatus, Integer errorCode) {
        this.name = name;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    KalaariErrorCode(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}