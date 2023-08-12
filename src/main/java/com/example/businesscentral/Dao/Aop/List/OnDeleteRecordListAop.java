package com.example.businesscentral.Dao.Aop.List;

import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.DeleteRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Aspect
@Configuration
public class OnDeleteRecordListAop {

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

        Boolean deleteSuccess = true;

        for (LinkedHashMap<String, Object> record : parameter.getRecords()) {
            Object Record = objectMapper.convertValue(record, tableBeanClass);
            List<Class<?>> classList = new ArrayList<>();
            classList.add(tableBean.getClass());
            BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
            businessCentralRecord.Reset();
            businessCentralRecord.SetRecord(Record);
            if (!businessCentralRecord.Delete()) {
                deleteSuccess = false;
            }
        }

        return deleteSuccess;
    }
}
