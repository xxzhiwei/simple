package com.aojiaodage.util;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RespUtil {
    private RespUtil() {}
    public static void output(HttpServletResponse response, String message) throws IOException {
        output(response, message, HttpStatus.OK.value());
    }

    public static void output(HttpServletResponse response, String message, Integer code) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(message);
    }
}
