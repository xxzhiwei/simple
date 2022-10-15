package com.aojiaodage.exception;

public class TokenExpired extends CustomException {
    public static final Integer CODE = 40198;
    public static final String DEFAULT_MESSAGE = "令牌已过期，请重新登录";

    public TokenExpired() {
        super(DEFAULT_MESSAGE, CODE);
    }

    public TokenExpired(String message) {
        super(message);
    }
}
