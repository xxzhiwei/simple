package com.aojiaodage.exception;

public class WrongTokenException extends CustomException {
    public static final Integer CODE = 40199;
    public static final String DEFAULT_MESSAGE = "非法的token";

    public WrongTokenException() {
        super(DEFAULT_MESSAGE, CODE);
    }

    public WrongTokenException(String message) {
        super(message);
    }
}
