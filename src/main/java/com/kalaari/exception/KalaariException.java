package com.kalaari.exception;

import lombok.Getter;
import lombok.ToString;

@ToString
public class KalaariException extends Exception {

    private static final long serialVersionUID = 1L;
    @Getter
    private KalaariErrorCode code;

    public KalaariException(KalaariErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public KalaariException(KalaariErrorCode code, String message, Throwable e) {
        super(message);
        this.setStackTrace(e.getStackTrace());
        this.code = code;
    }

    public KalaariException(KalaariErrorCode code, Throwable e) {
        super(e.getMessage());
        this.setStackTrace(e.getStackTrace());
        this.code = code;
    }
}