package com.aojiaodage.exception;

public class NotLoggedInException extends CustomException {
    public static final String DEFAULT_MESSAGE = "未登录";
    public static final Integer CODE = 40101;

    public NotLoggedInException() {
        super(DEFAULT_MESSAGE, CODE);
    }

    public NotLoggedInException(String message) {
        super(message, CODE);
    }
}
