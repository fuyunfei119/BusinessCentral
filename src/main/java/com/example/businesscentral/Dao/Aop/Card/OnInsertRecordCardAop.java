package com.example.businesscentral.Dao.Aop.Card;

import com.example.businesscentral.Dao.Annotation.OnInit;
import com.example.businesscentral.Dao.Annotation.OnNewRecord;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Enum.DataType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.CardField;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Request.CardParameter;
import com.example.businesscentral.Dao.Response.CardAfterGetCurrRecordResponse;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Configuration
public class OnInsertRecordCardAop {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @Pointcut("execution(* com.example.businesscentral.Controller.CustomerController.InsertNewRecord(..))")
    public void OnNewRecordTrigger() {
    }

    @Around("OnNewRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        CardParameter parameter = (CardParameter) joinPoint.getArgs()[0];

        System.out.println(parameter);

        return joinPoint.proceed();
    }
}
