package com.logos.args.exception;

public class NoParamsException extends RuntimeException {
    public NoParamsException(String message) {
        super(String.format("error type: %s", message));
    }
}
