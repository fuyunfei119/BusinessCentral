package com.example.businesscentral.Annotation;

import com.sun.jdi.Method;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.javassist.bytecode.annotation.ClassMemberValue;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.lang.invoke.MethodType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnInit {
    String value() default "";
}
