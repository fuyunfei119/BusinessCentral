package com.example.businesscentral.Annotation;

import com.example.businesscentral.System.DeleteTriggerType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnDelete {
    DeleteTriggerType value();
}
