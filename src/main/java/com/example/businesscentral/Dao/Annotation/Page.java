package com.example.businesscentral.Dao.Annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Page {
    @AliasFor(
            annotation = Component.class
    )
    String SOURCETABLE() default "";
}
