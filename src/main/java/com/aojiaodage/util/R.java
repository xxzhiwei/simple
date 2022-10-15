package com.aojiaodage.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class R<T> {
    private Integer code;
    private String message;
    private T data;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public R() {}

    public R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.setCode(0);
        r.setMessage("ok!");
        return r;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = ok();
        r.setData(data);
        return r;
    }

    public static <T> R<Map<String, Object>> ok(Page<T> page) {

        Map<String, Object> map = new HashMap<>();
        long pages = page.getPages();
        long total = page.getTotal();
        List<T> records = page.getRecords();

        map.put("pages", pages);
        map.put("total", total);
        map.put("records", records);

        return ok(map);
    }

    public static <T> R<T> error() {
        R<T> r = new R<>();
        r.setCode(1);
        r.setMessage("an error happened.");
        return r;
    }
}
