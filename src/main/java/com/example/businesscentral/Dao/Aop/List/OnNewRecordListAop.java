package com.example.businesscentral.Dao.Aop.List;

import com.example.businesscentral.Dao.Annotation.OnInit;
import com.example.businesscentral.Dao.Annotation.OnNewRecord;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.NewRecord;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class OnNewRecordListAop {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.OnNewRecord(..))")
    public void OnNewRecordTrigger() {
    }

    @Around("OnNewRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        NewRecord parameter = (NewRecord) joinPoint.getArgs()[0];

        Object pageBean = applicationContext.getBean(parameter.getPage());
        Object tableBean = applicationContext.getBean(parameter.getTable().toLowerCase(Locale.ROOT));

        Class<?> pageBeanClass = pageBean.getClass();
        Class<?> tableBeanClass = tableBean.getClass();

        Object newtableInstance = tableBeanClass.getDeclaredConstructor().newInstance();

        Method onInitMethod = null;

        for (Method declaredMethod : tableBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnInit.class)) {
                onInitMethod = declaredMethod;
            }
        }

        onInitMethod.setAccessible(true);
        Object initializedNewRecord = onInitMethod.invoke(newtableInstance);

        Method onNewRecordMethod = null;

        for (Method declaredMethod : pageBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnNewRecord.class)) {
                onNewRecordMethod = declaredMethod;
                break;
            }
        }

        List<Class<?>> classList = new ArrayList<>();
        classList.add(tableBean.getClass());
        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
        businessCentralRecord.SetRecord(initializedNewRecord);

        onNewRecordMethod.setAccessible(true);
        Object invokePageObject = onNewRecordMethod.invoke(pageBean, businessCentralRecord);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<String> excludefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

        List<String> includefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

        LinkedHashMap<String,Object> linkedHashMap = objectMapper.convertValue(invokePageObject,LinkedHashMap.class);

        for (String excludefield : excludefields) {
            excludefield = excludefield.replaceFirst(String.valueOf(excludefield.charAt(0)),String.valueOf(excludefield.charAt(0)).toUpperCase(Locale.ROOT));
            linkedHashMap.remove(excludefield);
        }

        LinkedHashMap<String,Object> sortedLinkedHashMap = new LinkedHashMap<>();
        for (String includefield : includefields) {
            char firstChar = includefield.charAt(0);
            String LowerCase = String.valueOf(firstChar).toLowerCase(Locale.ROOT);
            sortedLinkedHashMap.put(includefield,linkedHashMap.get(includefield.replaceFirst(String.valueOf(includefield.charAt(0)),LowerCase)));
        }

        return sortedLinkedHashMap;
    }
}
