package com.example.businesscentral.Dao.Aop.Card;

import com.example.businesscentral.Dao.Annotation.OnDelete;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.CardParameter;
import com.example.businesscentral.Dao.Request.DeleteRecord;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

@Aspect
@Configuration
public class OnDeleteRecordCardAop {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.DeleteCardRecord(..))")
    public void OnDeleteRecordTrigger() {
    }

    @Around("OnDeleteRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        CardParameter parameter = (CardParameter) joinPoint.getArgs()[0];

        if (!parameter.getIsNewRecord()) {

            Object tableBean = applicationContext.getBean(parameter.getTable().toLowerCase(Locale.ROOT));
            Object pageBean = applicationContext.getBean(parameter.getPage());

            Class<?> tableBeanClass = tableBean.getClass();
            Class<?> pageBeanClass = pageBean.getClass();

            Object newRecord = tableBeanClass.getDeclaredConstructor().newInstance();
            Object newCard = pageBeanClass.getDeclaredConstructor().newInstance();

            List<Class<?>> classList = new ArrayList<>();
            classList.add(tableBean.getClass());
            BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
            businessCentralRecord.Get(parameter.getRecordID());

            Object recordAfterTableOnDelete = null;

            for (Method declaredMethod : tableBeanClass.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(OnDelete.class)) {
                    declaredMethod.setAccessible(true);
                    recordAfterTableOnDelete = declaredMethod.invoke(newRecord,businessCentralRecord.GetRecord());
                    break;
                }
            }

            return businessCentralRecord.Delete();

        }

        return joinPoint.proceed();
    }
}
