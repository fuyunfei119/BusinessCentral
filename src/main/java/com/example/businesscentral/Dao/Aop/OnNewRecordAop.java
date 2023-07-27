package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.OnFindRecord;
import com.example.businesscentral.Dao.Annotation.OnInit;
import com.example.businesscentral.Dao.Annotation.OnNewRecord;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.NewRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Configuration
public class OnNewRecordAop {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @Pointcut("execution(java.util.* com.example.businesscentral.Controller.CustomerController.OnNewRecord(..))")
    public void OnNewRecordTrigger() {
    }

    @Around("OnNewRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        NewRecord parameter = (NewRecord) joinPoint.getArgs()[0];

        System.out.println(parameter);

        Object pageBean = applicationContext.getBean(parameter.getPage());
        Object tableBean = applicationContext.getBean(parameter.getTable());

        Class<?> pageBeanClass = pageBean.getClass();
        Class<?> tableBeanClass = tableBean.getClass();

        Method onInitMethod = null;

        for (Method declaredMethod : tableBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnInit.class)) {
                onInitMethod = declaredMethod;
            }
        }

        onInitMethod.setAccessible(true);
        Object initializedNewRecord = onInitMethod.invoke(tableBean);
        System.out.println(initializedNewRecord);

        Method onNewRecordMethod = null;

        for (Method declaredMethod : pageBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnNewRecord.class)) {
                onNewRecordMethod = declaredMethod;
            }
        }

        onNewRecordMethod.setAccessible(true);
        onNewRecordMethod.invoke(pageBean);

        return joinPoint.proceed();
    }
}
