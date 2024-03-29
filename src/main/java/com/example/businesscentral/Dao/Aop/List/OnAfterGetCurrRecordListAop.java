package com.example.businesscentral.Dao.Aop.List;

import com.example.businesscentral.Dao.Annotation.OnAfterGetCurrRecord;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.TableParameter;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class OnAfterGetCurrRecordListAop {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.OnListUpdated(..))")
    public void OnAfterGetRecordTrigger() {
    }

    @Around("OnAfterGetRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        TableParameter parameter = (TableParameter) joinPoint.getArgs()[0];

        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Page.class).values();

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
                break;
            }
        }

        assert beanClass != null;

        List<Class<?>> classList = new ArrayList<>();
        classList.add(Instance.getClass());

        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);

        Object newInstance = beanClass.getDeclaredConstructor().newInstance();
        Object newRecord = Instance.getClass().getDeclaredConstructor().newInstance();

        for (Method declaredMethod : beanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnAfterGetCurrRecord.class)) {

                for (Map.Entry<String, Object> entry : parameter.getRecord().entrySet()) {
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
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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

                businessCentralRecord.SetRecord(newRecord);

                Object pageBean = applicationContext.getBean(parameter.getPage());
                Object invoke = declaredMethod.invoke(newInstance,businessCentralRecord);

                Object convertValue = objectMapper.convertValue(invoke, pageBean.getClass());

                LinkedHashMap<String,Object> result = new LinkedHashMap<>();

                for (Field declaredField : convertValue.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    if (!ObjectUtils.isEmpty(declaredField.get(convertValue))) {
                        result.put(declaredField.getName(),declaredField.get(convertValue));
                    }
                }

                return result;
            }
        }


        return joinPoint.proceed();
    }
}
