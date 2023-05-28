package com.example.businesscentral.Dao.Annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Transactional
public @interface CodeUnit {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
