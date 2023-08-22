package com.example.businesscentral.Dao.Aop.Card;

import com.example.businesscentral.Dao.Annotation.OnAfterGetRecord;
import com.example.businesscentral.Dao.Annotation.OnQueryClosePage;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.CardParameter;
import com.example.businesscentral.Dao.Request.TableParameter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Aspect
@Configuration
public class OnQueryClosePageCardAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.OnBeforeCardUnmount(..))")
    public void OnQueryCloseTrigger() {
    }

    @Around("OnQueryCloseTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        CardParameter parameter = (CardParameter) joinPoint.getArgs()[0];

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Object pageBean = applicationContext.getBean(parameter.getPage());
        Object tableBean = applicationContext.getBean(parameter.getTable());

        Object newpageInstance = pageBean.getClass().getDeclaredConstructor().newInstance();

        if (!parameter.getIsNewRecord()) {
            List<Class<?>> classList = new ArrayList<>();
            classList.add(tableBean.getClass());
            BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
            businessCentralRecord.Get(parameter.getRecordID());

            for (Method declaredMethod : pageBean.getClass().getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(OnQueryClosePage.class)) {
                    declaredMethod.invoke(newpageInstance,businessCentralRecord);
                    break;
                }
            }
        }else {

            List<Class<?>> classList = new ArrayList<>();
            classList.add(tableBean.getClass());
            BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);

            Object record = objectMapper.convertValue(parameter.getRecord(), tableBean.getClass());

            businessCentralRecord.SetRecord(record);
            return businessCentralRecord.Insert(true,true);
        }

        return joinPoint.proceed();
    }
}
