package com.example.businesscentral.Dao.Aop.Card;

import com.example.businesscentral.Dao.Annotation.OnAfterGetRecord;
import com.example.businesscentral.Dao.Annotation.OnQueryClosePage;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.CardParameter;
import com.example.businesscentral.Dao.Request.TableParameter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Aspect
@Configuration
public class OnQueryClosePageCardAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.util.List<*> com.example.businesscentral.Controller.CustomerController.OnBeforeCardUnmount(..))")
    public void OnQueryCloseTrigger() {
    }

    @Around("OnQueryCloseTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        CardParameter parameter = (CardParameter) joinPoint.getArgs()[0];

        Object pageBean = applicationContext.getBean(parameter.getPage());
        Object tableBean = applicationContext.getBean(parameter.getTable());

        Object newpageInstance = pageBean.getClass().getDeclaredConstructor().newInstance();

        List<Class<?>> classList = new ArrayList<>();
        classList.add(tableBean.getClass());
        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
        businessCentralRecord.Get(parameter.getRecordID());

        for (Method declaredMethod : pageBean.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnQueryClosePage.class)) {
                declaredMethod.invoke(newpageInstance,businessCentralRecord);
                break;
            }
        }

        return joinPoint.proceed();
    }
}
