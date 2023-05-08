package com.example.businesscentral.Annotation;

import com.example.businesscentral.System.ModifyTriggerType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface OnModify {
    ModifyTriggerType value();
}
