package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.DataType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.CardField;
import com.example.businesscentral.Dao.Request.CardFieldOption;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Request.CardPageID;
import com.example.businesscentral.Dao.Response.CardOptionResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.*;

@Aspect
@Configuration
public class CardFieldOptionAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.GetFieldOptionForCard(..))")
    public void beforeGetFieldOptionForCard() {
    }

    @Around("beforeGetFieldOptionForCard()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        CardFieldOption parameter = (CardFieldOption) joinPoint.getArgs()[0];

        System.out.println(parameter);

        Object tableBean = applicationContext.getBean(parameter.getTable());

        Class<?> tableBeanClass = tableBean.getClass();
        Field declaredField = tableBeanClass.getDeclaredField(parameter.getFieldName());

        CardOptionResponse cardOptionResponse = new CardOptionResponse();

        if (Enum.class.isAssignableFrom(declaredField.getType())) {
            cardOptionResponse.setFieldType("enum");

            List<Object> options = new ArrayList<>();

            Object[] enumConstants = declaredField.getType().getEnumConstants();

            options.addAll(Arrays.asList(enumConstants));

            cardOptionResponse.setFieldOptions(options);
        }

        return cardOptionResponse;
    }
}
