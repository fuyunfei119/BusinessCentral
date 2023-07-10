package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.OnAfterGetRecord;
import com.example.businesscentral.Dao.Annotation.OnNextRecord;
import com.example.businesscentral.Dao.Annotation.OnOpenPage;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Request.TableParameter;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Configuration
public class OnAfterGetRecordAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.util.List<*> com.example.businesscentral.Controller.CustomerController.OnBeforeUpdate(..))")
    public void OnAfterGetRecordTrigger() {
    }

    @Around("OnAfterGetRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        TableParameter table = (TableParameter) joinPoint.getArgs()[0];

        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Page.class).values();

        Class<?> beanClass = null;
        Object Instance = null;

        for (Object bean : beans) {
            Page annotation = bean.getClass().getAnnotation(Page.class);
            if (annotation.SOURCETABLE().equals(table.getTable()) && annotation.TYPE().equals(PageType.List)) {
                beanClass = bean.getClass();
                Instance = applicationContext.getBean(table.getTable().toLowerCase(Locale.ROOT));
            }
        }

        assert beanClass != null;

        Object newInstance = beanClass.getDeclaredConstructor().newInstance();


        for (Method declaredMethod : beanClass.getDeclaredMethods()) {

            if (declaredMethod.isAnnotationPresent(OnAfterGetRecord.class)) {

                for (LinkedHashMap<String, Object> record : table.getRecords()) {

                    Object newRecord = Instance.getClass().getDeclaredConstructor().newInstance();

                    Iterator<Map.Entry<String, Object>> iterator = record.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<String, Object> entry = iterator.next();

                        Field declaredField = newRecord.getClass().getDeclaredField(BusinessCentralUtils.convertToCamelCase(entry.getKey()));
                        declaredField.setAccessible(true);
                        declaredField.set(newRecord,entry.getValue());
                    }

                    Object invoke = declaredMethod.invoke(newInstance,newRecord);
                }
            }
        }

        return joinPoint.proceed();
    }
}
