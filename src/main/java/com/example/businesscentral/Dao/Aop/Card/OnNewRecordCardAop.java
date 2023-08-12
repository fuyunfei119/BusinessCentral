package com.example.businesscentral.Dao.Aop.Card;

import com.example.businesscentral.Dao.Annotation.OnInit;
import com.example.businesscentral.Dao.Annotation.OnNewRecord;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Enum.DataType;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Request.CardField;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Request.CardParameter;
import com.example.businesscentral.Dao.Request.NewRecord;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Configuration
public class OnNewRecordCardAop {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @Pointcut("execution(java.*.* com.example.businesscentral.Controller.CustomerController.InsertNewRecordCard(..))")
    public void OnNewRecordTrigger() {
    }

    @Around("OnNewRecordTrigger()")
    public Object OnInitNewRecord(ProceedingJoinPoint joinPoint) throws Throwable {

        CardParameter parameter = (CardParameter) joinPoint.getArgs()[0];

        Object pageBean = applicationContext.getBean(parameter.getPage());
        Object tableBean = applicationContext.getBean(parameter.getTable().toLowerCase(Locale.ROOT));

        Class<?> pageBeanClass = pageBean.getClass();
        Class<?> tableBeanClass = tableBean.getClass();

        Object newtableInstance = tableBeanClass.getDeclaredConstructor().newInstance();

        Method onInitMethod = null;

        for (Method declaredMethod : tableBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnInit.class)) {
                onInitMethod = declaredMethod;
            }
        }

        onInitMethod.setAccessible(true);
        Object initializedNewRecord = onInitMethod.invoke(newtableInstance);

        Method onNewRecordMethod = null;

        for (Method declaredMethod : pageBeanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnNewRecord.class)) {
                onNewRecordMethod = declaredMethod;
                break;
            }
        }

        List<Class<?>> classList = new ArrayList<>();
        classList.add(tableBean.getClass());
        BusinessCentralRecord businessCentralRecord = new BusinessCentralRecordMySql(applicationContext,classList);
        businessCentralRecord.SetRecord(initializedNewRecord);

        onNewRecordMethod.setAccessible(true);
        Object invokePageObject = onNewRecordMethod.invoke(pageBean, businessCentralRecord);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object cardRecord = objectMapper.convertValue(invokePageObject, pageBean.getClass());

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
