package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.OnDelete;
import com.example.businesscentral.Dao.Annotation.OnInsert;
import com.example.businesscentral.Dao.Annotation.OnInsertRecord;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.DeleteRecord;
import com.example.businesscentral.Dao.Request.NewRecord;
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
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Configuration
public class OnDeleteRecordAop {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.OnDeleteRecord(..))")
    public void OnDeleteRecordTrigger() {
    }

    @Around("OnDeleteRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        DeleteRecord parameter = (DeleteRecord) joinPoint.getArgs()[0];

        Object tableBean = applicationContext.getBean(parameter.getTable());
        Class<?> tableBeanClass = tableBean.getClass();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        Object Record = objectMapper.convertValue(parameter.getRecord(), tableBeanClass);

        System.out.println(Record);

        List<Class<?>> classList = new ArrayList<>();
        classList.add(tableBean.getClass());
        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
        businessCentralRecord.SetRecord(Record);
        return businessCentralRecord.Delete();
    }
}
