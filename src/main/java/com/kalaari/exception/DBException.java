package com.kalaari.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DBException extends KalaariException {

    public DBException(KalaariErrorCode code, String message, DataIntegrityViolationException e) {
        super(code, message, e);
    }
}
