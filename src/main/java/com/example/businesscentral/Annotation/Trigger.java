package com.example.businesscentral.Annotation;

import com.example.businesscentral.System.SystemTriggerType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Trigger {

    String MethodName() default "";

    SystemTriggerType RaisedFor();
}