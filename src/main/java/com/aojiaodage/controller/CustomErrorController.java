package com.aojiaodage.controller;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomErrorController extends BasicErrorController {
    public CustomErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, ErrorAttributeOptions options) {
        Map<String, Object> r = new HashMap<>();
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        Object exception = errorAttributes.get("exception");
        Object errors = errorAttributes.get("errors");

        StringBuilder stringBuilder = new StringBuilder();

        if (exception != null) {
            stringBuilder.append("【").append(exception).append("】");
        }

        if (errors != null) {
            // 已确认是List<ObjectError>
            List<ObjectError> errorList = (List<ObjectError>) errors;

            for (int i=0; i<errorList.size(); i++) {
                String defaultMessage = errorList.get(i).getDefaultMessage();
                stringBuilder.append(defaultMessage);
                if (i != errorList.size() - 1) {
                    stringBuilder.append(",");
                }
            }
        }
        else {
            Object messageObj = errorAttributes.get("message");
            if (messageObj != null) {
                stringBuilder.append(messageObj);
            }
        }

        if (stringBuilder.length() == 0) {
            stringBuilder.append("服务器出错");
        }
        r.put("message", stringBuilder.toString());
        r.put("code", errorAttributes.get("status"));
        r.put("data", null);
        return r;
    }
}
