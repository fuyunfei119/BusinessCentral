package com.example.businesscentral.Annotation;

import com.example.businesscentral.System.ValidateTriggerType;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnValidate {
    ValidateTriggerType value();
}
