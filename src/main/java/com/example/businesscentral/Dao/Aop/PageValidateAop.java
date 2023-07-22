package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.Annotation.TableField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.PageValidate;
import com.example.businesscentral.Table.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.tomcat.util.buf.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Configuration
public class PageValidateAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.util.* com.example.businesscentral.Controller.CustomerController.PageFieldValidate(..))")
    public void BeforeValidatePageField() {
    }

    @Around("BeforeValidatePageField()")
    public Object OnBeforeValidatePageField(ProceedingJoinPoint joinPoint) throws Throwable {

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(new PropertyNamingStrategies.UpperCamelCaseStrategy());

        PageValidate parametes = (PageValidate) joinPoint.getArgs()[0];

        Object tableBean = applicationContext.getBean(parametes.getTable().toLowerCase(Locale.ROOT));

        Object pageBean = applicationContext.getBean(parametes.getPage());

        Field tableField = ReflectionUtils.findField(tableBean.getClass(), parametes.getField());

        Field pageField = ReflectionUtils.findField(pageBean.getClass(), parametes.getField());

        TableField tableFieldAnnotation = tableField.getAnnotation(TableField.class);

        Object record = objectMapper.convertValue(parametes.getRecord(), tableBean.getClass());

        List<Class<?>> classList = new ArrayList<>();
        classList.add(tableBean.getClass());
        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
        businessCentralRecord.SetXRecord(record);

        Method tableValidate = ReflectionUtils.findMethod(
                tableBean.getClass(),
                tableFieldAnnotation.ON_VALIDATE(),
                Object.class,
                Object.class,
                tableBean.getClass()
        );

        tableValidate.setAccessible(true);

        Object recordAfterTableValidate = tableValidate.invoke(tableBean, parametes.getCurrentValue(), parametes.getNewValue(), record);

        businessCentralRecord.SetCurrentRecord(recordAfterTableValidate);

        businessCentralRecord.Modify(true);

        Object recordAfterDatabaseModify = businessCentralRecord.GetRecord();

        Field declaredField = recordAfterDatabaseModify.getClass().getDeclaredField(parametes.getField());
        declaredField.setAccessible(true);
        Object FieldNewValue = declaredField.get(recordAfterDatabaseModify);

        PageField pageFieldAnnotation = pageField.getAnnotation(PageField.class);

        Method pageValidate = ReflectionUtils.findMethod(
                pageBean.getClass(),
                pageFieldAnnotation.ON_VALIDATE(),
                Object.class,
                Object.class,
                BusinessCentralRecord.class
        );

        pageValidate.setAccessible(true);

        Object recordAfterPageValidate = pageValidate.invoke(pageBean, parametes.getCurrentValue(), FieldNewValue, businessCentralRecord);

        LinkedHashMap<String,Object> linkedHashMap = objectMapper.convertValue(recordAfterPageValidate, LinkedHashMap.class);
        return linkedHashMap;
    }
}
