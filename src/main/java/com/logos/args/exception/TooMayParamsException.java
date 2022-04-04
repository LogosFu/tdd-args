package com.logos.args.exception;

public class TooMayParamsException extends RuntimeException {

    public TooMayParamsException(String message) {
        super("error type: " + message);
    }
}
