package com.example.businesscentral.Dao.Aop;

import com.example.businesscentral.Dao.Annotation.OnAfterGetCurrRecord;
import com.example.businesscentral.Dao.Annotation.OnAfterGetRecord;
import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.DataType;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.CardField;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Request.CardParameter;
import com.example.businesscentral.Dao.Request.TableParameter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
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
public class OnAfterGetCurrRecordCardAop {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(java.util.* com.example.businesscentral.Controller.CustomerController.OnCardUpdated(..))")
    public void OnAfterGetCurrRecordTrigger() {
    }

    @Around("OnAfterGetCurrRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        CardParameter parameter = (CardParameter) joinPoint.getArgs()[0];

        Object pageBean = applicationContext.getBean(parameter.getPage());
        Object tableBean = applicationContext.getBean(parameter.getTable());

        Object newpageInstance = pageBean.getClass().getDeclaredConstructor().newInstance();


        List<Class<?>> classList = new ArrayList<>();
        classList.add(tableBean.getClass());
        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
        businessCentralRecord.Get(parameter.getRecordID());

        Object recordAfterOnAfterGetRecordMethod = null;

        for (Method declaredMethod : pageBean.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnAfterGetCurrRecord.class)) {
                recordAfterOnAfterGetRecordMethod = declaredMethod.invoke(newpageInstance,businessCentralRecord);
                break;
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object cardRecord = objectMapper.convertValue(recordAfterOnAfterGetRecordMethod, pageBean.getClass());

        List<String> GroupNames = new ArrayList<>();
        List<CardGroup> cardGroups = new ArrayList<>();

        List<String> excludefields = Arrays.stream(pageBean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class)).map(field -> field.getName()).toList();

        List<Field> fields = Arrays.stream(pageBean.getClass().getDeclaredFields())
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
            LinkedHashMap<String, CardField> map = new LinkedHashMap<>();

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
