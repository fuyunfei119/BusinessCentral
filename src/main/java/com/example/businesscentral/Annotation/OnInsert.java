package com.example.businesscentral.Annotation;

import com.example.businesscentral.System.InsertTriggerType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnInsert {
    InsertTriggerType value();
}
