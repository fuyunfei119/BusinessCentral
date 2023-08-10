package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.DataType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.CardField;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Request.CardPageID;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
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

        CardPageID parameter = (CardPageID) joinPoint.getArgs()[0];
        String tableName = parameter.getTable();
        String recordID = parameter.getRecordID();
        String cardID = parameter.getCardID();

        Object tableBean = applicationContext.getBean(tableName);
        Object cardBean = applicationContext.getBean(cardID);

        Object newInstance = cardBean.getClass().getDeclaredConstructor().newInstance();

        List<Class<?>> classList = new ArrayList<>();
        classList.add(tableBean.getClass());
        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
        Object Record = businessCentralRecord.Get(recordID);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object cardRecord = objectMapper.convertValue(Record, cardBean.getClass());

        List<String> GroupNames = new ArrayList<>();
        List<CardGroup> cardGroups = new ArrayList<>();

        List<String> excludefields = Arrays.stream(cardBean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

        List<Field> fields = Arrays.stream(cardBean.getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Autowired.class)).toList();

        for (Field field : fields) {
            if (field.isAnnotationPresent(PageField.class)) {
                PageField pageField = field.getAnnotation(PageField.class);
                if (!GroupNames.contains(pageField.GROUP())) {
                    GroupNames.add(pageField.GROUP());
                }
            }
        }

        for (String groupName : GroupNames) {

            CardGroup cardGroup = new CardGroup();
            cardGroup.setGroupName(groupName);
            LinkedHashMap<String,CardField> map = new LinkedHashMap<>();

            for (Field field : fields) {
                if (field.isAnnotationPresent(PageField.class)) {
                    PageField pageField = field.getAnnotation(PageField.class);
                    if (groupName.equals(pageField.GROUP())) {

                        CardField cardField = new CardField();
                        field.setAccessible(true);

                        if (field.getType().isAssignableFrom(String.class)) {
                            cardField.setType(DataType.string);
                        }else if(field.getType().isAssignableFrom(Integer.class) ||field.getType().isAssignableFrom(Double.class) || field.getType().isAssignableFrom(Float.class)) {
                            cardField.setType(DataType.number);
                        }else if (field.getType().isAssignableFrom(Date.class) || field.getType().isAssignableFrom(java.sql.Date.class)) {
                            cardField.setType(DataType.date);
                        }else if (Enum.class.isAssignableFrom(field.getType())) {
                            cardField.setType(DataType.enumeration);
                        }

                        cardField.setValue(field.get(cardRecord));
                        map.put(field.getName(), cardField);
                    }
                }
            }

            cardGroup.setFields(map);
            cardGroups.add(cardGroup);
        }

        return cardGroups;
    }
}
