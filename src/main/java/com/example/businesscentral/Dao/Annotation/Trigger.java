package com.example.businesscentral.Dao.Annotation;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Trigger {

    String MethodName() default "";

}