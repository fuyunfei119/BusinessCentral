package com.example.businesscentral.Dao.Aop.List;

import com.example.businesscentral.Dao.Annotation.OnAfterGetRecord;
import com.example.businesscentral.Dao.Annotation.OnNextRecord;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.TableParameter;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Aspect
@Configuration
public class OnAfterGetRecordListAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.OnBeforeListUpdate(..))")
    public void OnAfterGetRecordTrigger() {
    }

    @Around("OnAfterGetRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        TableParameter parameter = (TableParameter) joinPoint.getArgs()[0];

        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Page.class).values();
        Object pageBean = applicationContext.getBean(parameter.getPage());

        Class<?> beanClass = null;
        Object Instance = null;
        Class<?> Record = null;

        for (Object bean : beans) {
            Page annotation = bean.getClass().getAnnotation(Page.class);
            if (annotation.SOURCETABLE().equals(parameter.getTable()) && annotation.TYPE().equals(PageType.List)) {
                beanClass = bean.getClass();
                for (Field declaredField : bean.getClass().getDeclaredFields()) {
                    if (declaredField.isAnnotationPresent(Autowired.class)) {
                        Record = declaredField.getType();
                    }
                }
                Instance = applicationContext.getBean(parameter.getTable());
            }
        }

        assert Record != null;

        List<Class<?>> classList = new ArrayList<>();
        classList.add(Instance.getClass());

        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);

        Object newInstance = beanClass.getDeclaredConstructor().newInstance();

        Method OnAfterGetRecordMethod = null;
        Method OnNextRecordMethod = null;

        for (Method declaredMethod : beanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnAfterGetRecord.class)) {
                OnAfterGetRecordMethod = declaredMethod;
            }

            if (declaredMethod.isAnnotationPresent(OnNextRecord.class)) {
                OnNextRecordMethod = declaredMethod;
            }
        }

        Iterator<LinkedHashMap<String, Object>> iterator = parameter.getRecords().iterator();

        Integer Steps = null;

        List<LinkedHashMap<String,Object>> results = new ArrayList<>();

        while (iterator.hasNext())
        {
            LinkedHashMap<String, Object> record = null;

            if (ObjectUtils.isEmpty(Steps)) {
                record = iterator.next();
            }else {
                for (int i = 0; i < Steps; i++) {
                    if (iterator.hasNext()) {
                        record = iterator.next();
                    }
                }
            }

            Object newRecord = Instance.getClass().getDeclaredConstructor().newInstance();

            assert record != null;

            for (Map.Entry<String, Object> entry : record.entrySet()) {
                Field declaredField = newRecord.getClass().getDeclaredField(entry.getKey());
                declaredField.setAccessible(true);
                if (Enum.class.isAssignableFrom(declaredField.getType())) {
                    Class<?> enumType = declaredField.getType();
                    Object enumValue = null;
                    if (!ObjectUtils.isEmpty(entry.getValue())) {
                        enumValue = Enum.valueOf((Class<Enum>) enumType, entry.getValue().toString());
                    }
                    declaredField.set(newRecord, enumValue);
                }
                else if (Date.class.isAssignableFrom(declaredField.getType()) || java.sql.Date.class.isAssignableFrom(declaredField.getType()) ) {
                    if (!ObjectUtils.isEmpty(entry.getValue())) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-SS");
                        declaredField.set(newRecord, dateFormat.parse(entry.getValue().toString()));
                    }
                } else if (Double.class.isAssignableFrom(declaredField.getType())) {
                    if (!ObjectUtils.isEmpty(entry.getValue())) {
                        declaredField.set(newRecord, Double.valueOf(entry.getValue().toString()));
                    }
                }
                else {
                    declaredField.set(newRecord,entry.getValue());
                }
            }

            assert OnAfterGetRecordMethod != null;

            businessCentralRecord.SetRecord(newRecord);

            Object invoke = OnAfterGetRecordMethod.invoke(newInstance,businessCentralRecord);

            Object convertValue = objectMapper.convertValue(invoke, pageBean.getClass());

            LinkedHashMap<String,Object> result = new LinkedHashMap<>();

            for (Field declaredField : convertValue.getClass().getDeclaredFields()) {
                declaredField.setAccessible(true);
                if (!ObjectUtils.isEmpty(declaredField.get(convertValue))) {
                    result.put(declaredField.getName(),declaredField.get(convertValue));
                }
            }

            if (ObjectUtils.isEmpty(Steps)) {
                Steps = (Integer) OnNextRecordMethod.invoke(newInstance, 1);
            }else {
                Steps = (Integer) OnNextRecordMethod.invoke(newInstance, Steps);
            }

            results.add(result);
        }

        return results;
    }
}
