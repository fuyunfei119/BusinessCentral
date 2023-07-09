package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.BusinessCentralPage;
import com.example.businesscentral.Dao.Enum.PageData;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.PageData.CustomerPageData;
import com.example.businesscentral.Dao.ProtoType.PageMySql;
import com.example.businesscentral.Dao.Request.CardGroup;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Configuration
public class ListAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.util.List<*> com.example.businesscentral.Controller.CustomerController.FindSetForList(..))")
    public void beforeInitNewRecord() {
    }

    @Around("beforeInitNewRecord()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        Object listName = joinPoint.getArgs()[0];

        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Page.class).values();

        PageMySql pageMysql = applicationContext.getBean(PageMySql.class);

        for (Object bean : beans) {
            for (Annotation annotation : bean.getClass().getAnnotations()) {
                Page page = (Page) annotation;

                if (page.SOURCETABLE().equals(listName) && page.TYPE().equals(PageType.List)) {
                    Class<?> aClass = bean.getClass();
                    for (Field declaredField : aClass.getDeclaredFields()) {
                        if (declaredField.isAnnotationPresent(PageField.class)) {
                            PageField pageField = declaredField.getAnnotation(PageField.class);
                            if (pageField.VISIABLE()) {
                                pageMysql.SetLoadFields(declaredField.getName());
                            }
                        }
                    }
                }
            }
        }

        List list = pageMysql.FindSet();
        return list;
    }
}
