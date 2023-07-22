package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.Annotation.TableField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.PageValidate;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<String> excludefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

        List<String> includefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

        Object result = objectMapper.convertValue(recordAfterPageValidate, pageBean.getClass());

        LinkedHashMap<String,Object> linkedHashMap = objectMapper.convertValue(result,LinkedHashMap.class);
        for (String excludefield : excludefields) {
            excludefield = excludefield.replaceFirst(String.valueOf(excludefield.charAt(0)),String.valueOf(excludefield.charAt(0)).toUpperCase(Locale.ROOT));
            linkedHashMap.remove(excludefield);
        }

        LinkedHashMap<String,Object> sortedLinkedHashMap = new LinkedHashMap<>();
        for (String includefield : includefields) {
            sortedLinkedHashMap.put(includefield,linkedHashMap.get(includefield));
        }

        return sortedLinkedHashMap;
    }
}