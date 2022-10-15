package com.aojiaodage.exception;

public class WrongUsernameOrPwdException extends CustomException {
    public static final String DEFAULT_MESSAGE = "账号或密码错误";
    public static final Integer CODE = 40001;

    public WrongUsernameOrPwdException() {
        super(DEFAULT_MESSAGE, CODE);
    }

    public WrongUsernameOrPwdException(String message) {
        super(message, CODE);
    }
}
