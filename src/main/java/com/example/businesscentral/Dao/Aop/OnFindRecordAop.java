package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.OnFindRecord;
import com.example.businesscentral.Dao.Annotation.OnOpenPage;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Request.TableParameter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

@Aspect
@Configuration
public class OnFindRecordAop {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @Pointcut("execution(java.util.List<*> com.example.businesscentral.Controller.CustomerController.OnMounted(..))")
    public void OnOpenPageTrigger() {
    }

    @Around("OnOpenPageTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        Object table = joinPoint.getArgs()[0];

        System.out.println("==============>");
        System.out.println(table);

        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Page.class).values();

        Class<?> beanClass = null;

        for (Object bean : beans) {
            Page annotation = bean.getClass().getAnnotation(Page.class);
            if (annotation.SOURCETABLE().equals(table) && annotation.TYPE().equals(PageType.List)) {
                beanClass = bean.getClass();
            }
        }

        assert beanClass != null;

        Object newInstance = beanClass.getDeclaredConstructor().newInstance();

        for (Method declaredMethod : beanClass.getDeclaredMethods()) {

            if (declaredMethod.isAnnotationPresent(OnFindRecord.class)) {

                List<LinkedHashMap<String, Object>> records = businessCentralSystemRecord.GetDataForListPage(table.toString());

                return declaredMethod.invoke(newInstance,records);
            }
        }

        return joinPoint.proceed();
    }
}
