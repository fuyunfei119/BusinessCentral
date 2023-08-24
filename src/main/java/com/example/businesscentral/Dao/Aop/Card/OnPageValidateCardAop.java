package com.example.businesscentral.Dao.Aop.Card;

import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.Annotation.TableField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.DataType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.CardField;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Request.CardParameter;
import com.example.businesscentral.Dao.Request.PageValidate;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Aspect
@Configuration
public class OnPageValidateCardAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.UpdateRecordAfterCardValidate(..))")
    public void BeforeValidateCardPageField() {
    }

    @Around("BeforeValidateCardPageField()")
    public Object OnBeforeValidatePageField(ProceedingJoinPoint joinPoint) throws Throwable {

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(new PropertyNamingStrategies.UpperCamelCaseStrategy());

        CardParameter parametes = (CardParameter) joinPoint.getArgs()[0];

        System.out.println(parametes);

        Object tableBean = applicationContext.getBean(parametes.getTable());
        Object pageBean = applicationContext.getBean(parametes.getPage());

        Field tableField = ReflectionUtils.findField(tableBean.getClass(), parametes.getFieldName());

        Field pageField = ReflectionUtils.findField(pageBean.getClass(), parametes.getFieldName());

        TableField tableFieldAnnotation = tableField.getAnnotation(TableField.class);

        List<Class<?>> classList = new ArrayList<>();
        classList.add(tableBean.getClass());
        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
        Object record = businessCentralRecord.Get(parametes.getRecordID());
        businessCentralRecord.SetXRecord(record);

        Method tableValidate = ReflectionUtils.findMethod(
                tableBean.getClass(),
                tableFieldAnnotation.ON_VALIDATE(),
                Object.class,
                Object.class,
                tableBean.getClass()
        );

        Object recordAfterTableValidate = null;

        if (ObjectUtils.isEmpty(tableValidate)) {
            tableField.setAccessible(true);

            System.out.println(tableField.getType());

            if (Enum.class.isAssignableFrom(tableField.getType())) {
                Class<?> enumType = tableField.getType();

                Object enumValue = null;
                if (!ObjectUtils.isEmpty(parametes.getNewValue())) {
                    enumValue = Enum.valueOf((Class<Enum>) enumType, parametes.getNewValue().toString());
                }
                tableField.set(record, enumValue);
            }else if (Date.class.isAssignableFrom(tableField.getType())) {

                tableField.set(record, java.sql.Date.valueOf((String) parametes.getNewValue()));
            }else
                tableField.set(record,parametes.getNewValue());

            recordAfterTableValidate = record;
        }else {
            tableValidate.setAccessible(true);

            recordAfterTableValidate = tableValidate.invoke(
                    record,
                    parametes.getOldValue(),
                    parametes.getNewValue(),
                    record
            );
        }

        businessCentralRecord.SetCurrentRecord(recordAfterTableValidate);
        businessCentralRecord.Modify(true);

        Object recordAfterDatabaseModify = businessCentralRecord.GetRecord();

        Field declaredField = recordAfterDatabaseModify.getClass().getDeclaredField(parametes.getFieldName());
        declaredField.setAccessible(true);
        Object FieldNewValue = declaredField.get(recordAfterDatabaseModify);

        PageField pageFieldAnnotation = pageField.getAnnotation(PageField.class);

        Method pageValidate = ReflectionUtils.findMethod(
                pageBean.getClass(),
                pageFieldAnnotation.ON_VALIDATE(),
                Object.class,
                Object.class,
                BusinessCentralRecord.class
        );

        Object recordAfterPageValidate = null;

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (!ObjectUtils.isEmpty(pageValidate)) {
            pageValidate.setAccessible(true);
            recordAfterPageValidate = pageValidate.invoke(pageBean, parametes.getOldValue(), FieldNewValue, businessCentralRecord);
        }else {
            recordAfterPageValidate = objectMapper.convertValue(recordAfterDatabaseModify, pageBean.getClass());
        }

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Object cardRecord = objectMapper.convertValue(recordAfterPageValidate, pageBean.getClass());

        List<String> GroupNames = new ArrayList<>();
        List<CardGroup> cardGroups = new ArrayList<>();

        List<String> excludefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

        List<Field> fields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Autowired.class)).toList();

        for (Field field : fields) {
            if (field.isAnnotationPresent(PageField.class)) {
                PageField pageField1 = field.getAnnotation(PageField.class);
                if (!GroupNames.contains(pageField1.GROUP())) {
                    GroupNames.add(pageField1.GROUP());
                }
            }
        }

        for (String groupName : GroupNames) {

            CardGroup cardGroup = new CardGroup();
            cardGroup.setGroupName(groupName);
            LinkedHashMap<String, CardField> map = new LinkedHashMap<>();

            for (Field field : fields) {
                if (field.isAnnotationPresent(PageField.class)) {
                    PageField pageField1 = field.getAnnotation(PageField.class);
                    if (groupName.equals(pageField1.GROUP())) {

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
