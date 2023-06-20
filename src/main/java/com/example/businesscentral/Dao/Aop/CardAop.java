package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Request.CardGroup;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Configuration
public class CardAop {

    @Autowired
    private ApplicationContext applicationContext;


    @Pointcut("execution(java.util.LinkedHashMap<String,Object> com.example.businesscentral.Controller.CustomerController.GetRecordById(..))")
    public void beforeInitNewRecord() {
    }

    @Around("beforeInitNewRecord()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        Map<String,Object> table = (Map<String, Object>) joinPoint.getArgs()[0];
        Object tableName = table.get("table");
        Object recordID = table.get("RecordID");

        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Page.class).values();

        for (Object bean : beans) {
            for (Annotation annotation : bean.getClass().getAnnotations()) {
                Page page = (Page) annotation;
                if (page.SOURCETABLE().equals(tableName.toString()) && page.TYPE().equals(PageType.Card)) {
                    Method method = ReflectionUtils.findMethod(bean.getClass(), page.Method(),tableName.getClass(),recordID.getClass());
                    if (!ObjectUtils.isEmpty(method)) {
                        LinkedHashMap<String,Object> result = (LinkedHashMap<String, Object>) ReflectionUtils.invokeMethod(method, bean, tableName.toString(), recordID.toString());
                        return result;
                    }
                }
            }
        }

        return joinPoint.proceed();
    }
}
