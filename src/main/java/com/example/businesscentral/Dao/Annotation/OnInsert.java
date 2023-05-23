package com.example.businesscentral.Dao.Annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnInsert {
    String value() default "";
}
