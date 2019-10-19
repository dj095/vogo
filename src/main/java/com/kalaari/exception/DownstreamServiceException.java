package com.kalaari.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@ToString
public class DownstreamServiceException extends KalaariException {

    private static final long serialVersionUID = 1L;

    @Getter
    private HttpStatus httpStatus;

    public DownstreamServiceException(KalaariErrorCode code, String message, HttpStatus httpStatus, Throwable e) {
        super(code, message, e);
        this.httpStatus = httpStatus;
    }

    public DownstreamServiceException(KalaariErrorCode code, String message, HttpStatus httpStatus) {
        super(code, message);
        this.httpStatus = httpStatus;
    }

    public DownstreamServiceException(KalaariErrorCode code, String message) {
        super(code, message);
    }

    public DownstreamServiceException(KalaariErrorCode code, String message, Throwable e) {
        super(code, message, e);
    }
}