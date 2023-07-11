package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.OnAfterGetRecord;
import com.example.businesscentral.Dao.Annotation.OnNextRecord;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.TableParameter;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
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
        Class<?> Record = null;

        for (Object bean : beans) {
            Page annotation = bean.getClass().getAnnotation(Page.class);
            if (annotation.SOURCETABLE().equals(table.getTable()) && annotation.TYPE().equals(PageType.List)) {
                beanClass = bean.getClass();
                for (Field declaredField : bean.getClass().getDeclaredFields()) {
                    if (declaredField.isAnnotationPresent(Autowired.class)) {
                        Record = declaredField.getType();
                    }
                }
                Instance = applicationContext.getBean(table.getTable().toLowerCase(Locale.ROOT));
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

        Iterator<LinkedHashMap<String, Object>> iterator = table.getRecords().iterator();

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
                Field declaredField = newRecord.getClass().getDeclaredField(BusinessCentralUtils.convertToCamelCase(entry.getKey()));
                declaredField.setAccessible(true);
                declaredField.set(newRecord,entry.getValue());
            }

            assert OnAfterGetRecordMethod != null;

            businessCentralRecord.SetRecord(newRecord);

            Object invoke = OnAfterGetRecordMethod.invoke(newInstance,businessCentralRecord);

            LinkedHashMap<String,Object> result = new LinkedHashMap<>();

            for (Field declaredField : invoke.getClass().getDeclaredFields()) {
                declaredField.setAccessible(true);
                if (!ObjectUtils.isEmpty(declaredField.get(invoke))) {
                    result.put(declaredField.getName(),declaredField.get(invoke));
                }
            }

            if (ObjectUtils.isEmpty(Steps)) {
                Steps = (Integer) OnNextRecordMethod.invoke(newInstance, 1);
            }else {
                Steps = (Integer) OnNextRecordMethod.invoke(newInstance, Steps);
            }

//            System.out.println(result);

            results.add(result);
        }

        return results;
    }
}
