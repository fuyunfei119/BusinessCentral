package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Request.CardField;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.*;

@Aspect
@Configuration
public class CardAop {

    @Autowired
    private ApplicationContext applicationContext;


    @Pointcut("execution(java.util.List<*> com.example.businesscentral.Controller.CustomerController.GetRecordById(..))")
    public void beforeInitNewRecord() {
    }

    @Around("beforeInitNewRecord()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        Map<String,Object> table = (Map<String, Object>) joinPoint.getArgs()[0];
        Object tableName = table.get("table");
        Object recordID = table.get("RecordID");

        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Page.class).values();

        for (Object bean : beans) {
            for (Annotation annotation : bean.getClass().getAnnotations()) {
                Page page = (Page) annotation;
                if (page.SOURCETABLE().equals(tableName.toString()) && page.TYPE().equals(PageType.Card)) {
                    Method method = ReflectionUtils.findMethod(bean.getClass(), page.Method(),tableName.getClass(),recordID.getClass());
                    if (!ObjectUtils.isEmpty(method)) {
                        LinkedHashMap<String,Object> result = (LinkedHashMap<String, Object>) ReflectionUtils.invokeMethod(method, bean, tableName.toString(), recordID.toString());
                        List<CardGroup> cardGroups = OnAddGroupBeforeSendData(tableName.toString(), result);
                        return cardGroups;
                    }
                }
            }
        }

        return joinPoint.proceed();
    }

    private List<CardGroup> OnAddGroupBeforeSendData(String tableName,LinkedHashMap<String,Object> result) {

        List<String> GroupNames = new ArrayList<>();
        List<CardGroup> cardGroups = new ArrayList<>();

        Object bean = applicationContext.getBean(tableName.toLowerCase(Locale.ROOT));

        for (Field declaredField : bean.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(PageField.class)) {
                PageField annotation = declaredField.getAnnotation(PageField.class);
                if (!annotation.GROUP().isBlank()) {
                    if (!GroupNames.contains(annotation.GROUP())) {
                        GroupNames.add(annotation.GROUP());
                    }
                }
            }
        }

        for (String groupName : GroupNames) {

            CardGroup cardGroup = new CardGroup();
            LinkedHashMap<String, CardField> map = new LinkedHashMap<>();
            cardGroup.setGroupName(groupName);

            for (Field declaredField : bean.getClass().getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(PageField.class)) {
                    PageField annotation = declaredField.getAnnotation(PageField.class);
                    if (!annotation.GROUP().isBlank()) {
                        if (annotation.GROUP().equals(groupName)) {

                            CardField cardField = new CardField();
                            if (declaredField.getType().isAssignableFrom(String.class)) {
                                cardField.setType("Text");
                            }else if (declaredField.getType().isAssignableFrom(Integer.class) || 
                                      declaredField.getType().isAssignableFrom(Double.class) || 
                                      declaredField.getType().isAssignableFrom(BigDecimal.class) ||
                                    declaredField.getType().isAssignableFrom(BigInteger.class)) {
                                cardField.setType("number");
                            } else if (declaredField.getType().isAssignableFrom(Date.class)) {
                                cardField.setType("date");
                            } else if (declaredField.getType().isAssignableFrom(Time.class)) {
                                cardField.setType("time");
                            }
                            cardField.setValue(result.get(BusinessCentralUtils.convertToSnakeCase(declaredField.getName())));

                            map.put(declaredField.getName(), cardField);
                        }
                    }
                }
            }

            cardGroup.setFields(map);
            cardGroups.add(cardGroup);
        }

        return cardGroups;
    }
}
