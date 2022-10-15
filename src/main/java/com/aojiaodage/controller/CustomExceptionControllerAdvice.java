package com.aojiaodage.controller;

import com.aojiaodage.exception.*;
import com.aojiaodage.util.R;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionControllerAdvice {

    private R<?> makeR(CustomException exception) {
        exception.printStackTrace();
        R<?> r = R.error();
        r.setCode(exception.getCode());
        r.setMessage(exception.getMessage());
        return r;
    }

    private Map<String, String> getBindingErrorMsgMap(BindingResult bindingResult) {
        Map<String, String> errorMsgMap = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMsgMap.put(error.getField(), error.getDefaultMessage());
        }
        return errorMsgMap;
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handler(HttpRequestMethodNotSupportedException exception) {
        R<?> r = R.error();
        r.setCode(40501);
        r.setMessage("不支持的请求方法：" + exception.getMessage());
        return r;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public R<?> handler(BindException exception) {
        R<?> r = R.error();
        r.setCode(40002);
        r.setMessage("请求参数绑定失败：" + getBindingErrorMsgMap(exception.getBindingResult()));
        return r;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handler(MethodArgumentNotValidException exception) {
        R<?> r = R.error();
        r.setCode(40003);
        r.setMessage("请求参数校验失败：" + getBindingErrorMsgMap(exception.getBindingResult()));
        return r;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = NotLoggedInException.class)
    public R<?> handle(NotLoggedInException ex) {
        return makeR(ex);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = NoPermissionException.class)
    public R<?> handle(NoPermissionException ex) {
        return makeR(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = CustomException.class)
    public R<?> handle(CustomException ex) {
        return makeR(ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public R<?> handler(RuntimeException exception) {
        exception.printStackTrace();
        R<?> r = R.error();
        r.setCode(50001);
        r.setMessage("系统出现异常");
        return r;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public R<?> handler(Exception exception) {
        exception.printStackTrace();
        R<?> r = R.error();
        r.setCode(50002);
        r.setMessage("系统出现异常");
        return r;
    }
}
