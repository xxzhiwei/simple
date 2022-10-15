package com.aojiaodage.exception;

public class NoPermissionException extends CustomException {
    public static final String DEFAULT_MESSAGE = "无权限访问";
    public static final Integer CODE = 40301;

    public NoPermissionException() {
        super(DEFAULT_MESSAGE, CODE);
    }

    public NoPermissionException(String message) {
        super(message, CODE);
    }
}
