package com.example.businesscentral.Dao.Annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface TableField {
    String INIT_VALUE() default "";
    String ON_VALIDATE() default "";
    boolean NOT_BLANK() default false;
    FlowField FLOW_FIELD() default @FlowField();
    Keys KEYS() default @Keys();
}
