package com.example.businesscentral.Dao.Annotation;

import com.example.businesscentral.Dao.Enum.PageType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Page {
    String SOURCETABLE();
    PageType TYPE();
    String Method();
}
