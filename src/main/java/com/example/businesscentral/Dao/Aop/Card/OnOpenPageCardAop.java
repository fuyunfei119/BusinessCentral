package com.example.businesscentral.Dao.Aop.Card;

import com.example.businesscentral.Dao.Annotation.OnOpenPage;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Annotation.Table;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Request.TableParameter;
import com.example.businesscentral.Dao.Response.OnOpenPageResponse;
import com.example.businesscentral.Dao.Scanner.BusinessCentralPageScan;
import com.example.businesscentral.Dao.Scanner.BusinessCentralTableScan;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Aspect
@Configuration
public class OnOpenPageCardAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.onBeforeCardMounted(..))")
    public void OnInitTrigger() {
    }

    @Around("OnInitTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        TableParameter parameter = (TableParameter) joinPoint.getArgs()[0];
        OnOpenPageResponse onOpenPageResponse = new OnOpenPageResponse();

        Object tableBean = applicationContext.getBean(parameter.getTable());
        Object cardBean = null;

        Collection<Object> pageBeans = applicationContext.getBeansWithAnnotation(Page.class).values();

        for (Object pageBean : pageBeans) {
            if (parameter.getTable().equals(pageBean.getClass().getDeclaredAnnotation(Page.class).SOURCETABLE())
                    && pageBean.getClass().getDeclaredAnnotation(Page.class).TYPE().equals(PageType.Card)) {
                cardBean = pageBean;
                break;
            }
        }

        Class<?> cardBeanClass = cardBean.getClass();

        Object newInstance = cardBeanClass.getDeclaredConstructor().newInstance();

        for (Method declaredMethod : cardBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnOpenPage.class)) {
                Object invoked = declaredMethod.invoke(newInstance);
                break;
            }
        }

        return onOpenPageResponse;
    }
}
