package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.Page;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Aspect
@Configuration
public class CardAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.util.List<*> com.example.businesscentral.Controller.CustomerController.GetRecordById(..))")
    public void beforeInitNewRecord() {
    }

    @Around("beforeInitNewRecord()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        Map<String, Object> table = (Map<String, Object>) joinPoint.getArgs()[0];
        Object tableName = table.get("table");
        Object recordID = table.get("RecordID");

        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Page.class).values();
        return joinPoint.proceed();
    }
}
