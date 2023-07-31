package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.OnInit;
import com.example.businesscentral.Dao.Annotation.OnInsert;
import com.example.businesscentral.Dao.Annotation.OnInsertRecord;
import com.example.businesscentral.Dao.Annotation.OnNewRecord;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
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

import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Configuration
public class OnInsertRecordAop {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.OnInsertRecord(..))")
    public void OnInsertRecordTrigger() {
    }

    @Around("OnInsertRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        NewRecord parameter = (NewRecord) joinPoint.getArgs()[0];

        Object pageBean = applicationContext.getBean(parameter.getPage());
        Object tableBean = applicationContext.getBean(parameter.getTable());

        Class<?> pageBeanClass = pageBean.getClass();
        Class<?> tableBeanClass = tableBean.getClass();

        Method OnInsertRecordMethod = null;

        for (Method declaredMethod : pageBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnInsertRecord.class)) {
                OnInsertRecordMethod = declaredMethod;
                break;
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        Object Record = objectMapper.convertValue(parameter.getRecord(), tableBeanClass);

        List<Class<?>> classList = new ArrayList<>();
        classList.add(tableBean.getClass());
        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
        businessCentralRecord.SetRecord(Record);

        Object RecordAfterOnInsertRecord = OnInsertRecordMethod.invoke(pageBean, businessCentralRecord);

        businessCentralRecord.SetRecord(RecordAfterOnInsertRecord);

        Method OnInsert = null;

        for (Method declaredMethod : tableBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnInsert.class)) {
                OnInsert = declaredMethod;
                break;
            }
        }

        OnInsert.setAccessible(true);
        Object RecordAfterTableInsert = OnInsert.invoke(RecordAfterOnInsertRecord,RecordAfterOnInsertRecord);
        businessCentralRecord.SetRecord(RecordAfterOnInsertRecord);
        if (!businessCentralRecord.Insert(false,true)) {
            return joinPoint.proceed();
        }

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object convertValue = objectMapper.convertValue(RecordAfterTableInsert, pageBeanClass);

        List<String> excludefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

        List<String> includefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

        LinkedHashMap<String,Object> linkedHashMap = objectMapper.convertValue(convertValue,LinkedHashMap.class);

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
