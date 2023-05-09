package com.example.businesscentral.Annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApplicationArea {

    boolean All() default false;
}
