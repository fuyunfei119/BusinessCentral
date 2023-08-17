package com.example.businesscentral.Dao.Aop.List;

import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.Annotation.TableField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.PageValidate;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Aspect
@Configuration
public class OnPageValidateListAop {

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

        Object tableBean = applicationContext.getBean(parametes.getTable());

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

        Object recordAfterTableValidate = null;

        if (ObjectUtils.isEmpty(tableValidate)) {
            tableField.setAccessible(true);

            if (Enum.class.isAssignableFrom(tableField.getType())) {
                Class<?> enumType = tableField.getType();

                Object enumValue = null;
                if (!ObjectUtils.isEmpty(parametes.getNewValue())) {
                    enumValue = Enum.valueOf((Class<Enum>) enumType, parametes.getNewValue().toString());
                }
                tableField.set(record, enumValue);
            }
            else if (Date.class.isAssignableFrom(tableField.getType()) || java.sql.Date.class.isAssignableFrom(tableField.getType()) ) {
                if (!ObjectUtils.isEmpty(parametes.getNewValue())) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    tableField.set(record, dateFormat.parse(parametes.getNewValue().toString()));
                }
            } else if (Double.class.isAssignableFrom(tableField.getType())) {
                if (!ObjectUtils.isEmpty(parametes.getNewValue())) {
                    tableField.set(record, Double.valueOf(parametes.getNewValue().toString()));
                }
            }
            else
                tableField.set(record,parametes.getNewValue());

            recordAfterTableValidate = record;
        }else {
            tableValidate.setAccessible(true);

            recordAfterTableValidate = tableValidate.invoke(
                    record,
                    parametes.getCurrentValue(),
                    parametes.getNewValue(),
                    record
            );
        }

        businessCentralRecord.SetCurrentRecord(recordAfterTableValidate);

        if (!parametes.getNewRecord())
        {
            businessCentralRecord.Modify(true);
        }

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

        Object recordAfterPageValidate = null;

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (!ObjectUtils.isEmpty(pageValidate)) {
            pageValidate.setAccessible(true);
            recordAfterPageValidate = pageValidate.invoke(pageBean, parametes.getCurrentValue(), FieldNewValue, businessCentralRecord);
        }else {
            recordAfterPageValidate = objectMapper.convertValue(recordAfterDatabaseModify, pageBean.getClass());
        }

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
