package com.kalaari.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * THIS SHOULD BE THROWN ONLY WHEN WE WISH TO RETRY FROM A METHOD.
 */
@ToString
public class RetryException extends KalaariException {
    private static final long serialVersionUID = 1L;

    public RetryException(KalaariErrorCode code, String message) {
        super(code, message);
    }

    public enum RetryErrorCode {
        MS_LR_RECEIPT_FAILURE("Can't shift to this state because of No LR Receipt uploaded");

        @Getter
        private final String name;

        RetryErrorCode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}
