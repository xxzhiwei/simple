package com.aojiaodage.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { Field2InequalityCheckValidator.class })
public @interface Field2InequalityCheck {
    String field();
    String field2();

    // 下面是三个属性是JVB规范的必带属性，没有会报错
    String message() default "两字段的值不能相同";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
