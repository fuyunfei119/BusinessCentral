package com.example.businesscentral.Dao.Annotation;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Table {
    String value() default "";
}
