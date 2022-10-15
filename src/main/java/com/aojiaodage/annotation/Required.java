package com.aojiaodage.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Required {
    String value() default ""; // 这个可以考虑从外部文件读取【与数据库保持一致】，这样前端与后端可以同步进行更新；但是这东西一般定义了后，就不会轻易改动
}
