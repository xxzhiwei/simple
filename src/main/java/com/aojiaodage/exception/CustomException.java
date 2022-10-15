package com.aojiaodage.exception;

public class CustomException extends RuntimeException {
    private Integer code = 99999; // 40199

    public CustomException() {
        super("未知异常");
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
