package com.aojiaodage.validator;

import com.aojiaodage.util.ObjectToMapConverter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Objects;

// 校验两个字段必须一致
public class Field2ConsistencyCheckValidator implements ConstraintValidator<Field2ConsistencyCheck, Object> {
    private String field;
    private String field2;

    @Override
    public void initialize(Field2ConsistencyCheck constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.field2 = constraintAnnotation.field2();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Map<String, Object> map = ObjectToMapConverter.convert(value);
        // map.get(field).equals(map.get(field2)) // 用这个可能会出现空指针异常
        return Objects.equals(map.get(field), map.get(field2));
    }
}
