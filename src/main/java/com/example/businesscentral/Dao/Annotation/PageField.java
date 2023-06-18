package com.example.businesscentral.Dao.Annotation;

import java.lang.annotation.*;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface PageField {

    boolean VISIABLE() default false;

    String GROUP();
}
