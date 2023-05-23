package com.example.businesscentral.Dao.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Keys {

    boolean PRIMARY_KEY() default false;
    boolean SECOND_KEY() default false;
    boolean AUTO_INCREMENT() default false;
}
