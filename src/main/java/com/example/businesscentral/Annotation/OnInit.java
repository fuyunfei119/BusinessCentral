package com.example.businesscentral.Annotation;

import com.example.businesscentral.System.InitTriggerType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnInit {
    InitTriggerType value();
}
