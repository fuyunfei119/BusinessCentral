package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.OnAfterGetCurrRecord;
import com.example.businesscentral.Dao.Annotation.OnOpenPage;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.TableParameter;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import java.util.*;

@Aspect
@Configuration
public class OnAfterGetCurrRecordAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.util.* com.example.businesscentral.Controller.CustomerController.OnUpdated(..))")
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
                Instance = applicationContext.getBean(parameter.getTable().toLowerCase(Locale.ROOT));
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
                    declaredField.set(newRecord,entry.getValue());
                }

                businessCentralRecord.SetRecord(newRecord);

                Object invoke = declaredMethod.invoke(newInstance,businessCentralRecord);

                LinkedHashMap<String,Object> result = new LinkedHashMap<>();

                for (Field declaredField : invoke.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);
                    if (!ObjectUtils.isEmpty(declaredField.get(invoke))) {
                        result.put(declaredField.getName(),declaredField.get(invoke));
                    }
                }

                Object pageBean = applicationContext.getBean(parameter.getPage());

                List<String> excludefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

                List<String> includefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                        .filter(field -> !field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

                for (String excludefield : excludefields) {
                    excludefield = excludefield.replaceFirst(String.valueOf(excludefield.charAt(0)),String.valueOf(excludefield.charAt(0)).toUpperCase(Locale.ROOT));
                    result.remove(excludefield);
                }

                LinkedHashMap<String,Object> sortedLinkedHashMap = new LinkedHashMap<>();
                for (String includefield : includefields) {
                    sortedLinkedHashMap.put(includefield,result.get(includefield));
                }

                return sortedLinkedHashMap;
            }
        }


        return joinPoint.proceed();
    }
}
