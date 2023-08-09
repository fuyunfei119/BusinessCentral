package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.OnOpenPage;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Request.TableParameter;
import com.example.businesscentral.Dao.Response.OnOpenPageResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;

@Aspect
@Configuration
public class OnOpenPageAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.onBeforeMounted(..))")
    public void OnInitTrigger() {
    }

    @Around("OnInitTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        TableParameter parameter = (TableParameter) joinPoint.getArgs()[0];
        OnOpenPageResponse onOpenPageResponse = new OnOpenPageResponse();

        Object pageBean = applicationContext.getBean(parameter.getPage());
        Class<?> pageBeanClass = pageBean.getClass();

        String sourcetable = pageBeanClass.getAnnotation(Page.class).SOURCETABLE();
        if (StringUtils.hasLength(sourcetable)) {
            onOpenPageResponse.setTableName(sourcetable);
        }else {
            throw new Exception("No Valid table Name");
        }

        Object newInstance = pageBeanClass.getDeclaredConstructor().newInstance();

        for (Method declaredMethod : pageBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnOpenPage.class)) {
                Object invoke = declaredMethod.invoke(newInstance);
            }
        }

        return onOpenPageResponse;
    }
}
