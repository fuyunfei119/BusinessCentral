package com.example.businesscentral.Dao.Impl;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Mapper.BusinessCentralMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Repository
@Scope("prototype")
public class BusinessCentralRecordMySql<T,E extends Enum<E>> implements BusinessCentralRecord<T,E> {

    @Autowired
    private BusinessCentralMapper mapper;
    @Autowired
    private ApplicationContext applicationContext;
    private final List<String> filters = new ArrayList<>();
    private final List<String> loadfields = new ArrayList<>();
    private String tableName;
    private Field primaryKey;
    private Object keyValue;
    private List<T> entityList = new ArrayList<>();
    private Integer currentIndex;
    private T entity;
    private T x_entity;
    private Class<T> aClass;

    @Autowired
    public BusinessCentralRecordMySql(ApplicationContext applicationContext,List<Class<?>> entityClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (ObjectUtils.isEmpty(entityClass)) return;

        this.applicationContext = applicationContext;
        this.mapper = applicationContext.getBean(BusinessCentralMapper.class);
        this.aClass = (Class<T>) entityClass.get(0);
        this.tableName = this.aClass.getSimpleName();
        this.primaryKey = BusinessCentralUtils.getPrimaryKeyField(this.aClass);
        this.entity = this.aClass.getDeclaredConstructor().newInstance();
        this.x_entity = this.aClass.getDeclaredConstructor().newInstance();

        currentIndex = 0;
    }

    @Override
    public BusinessCentralRecord<T,E> SetRange(E entityFields, Object newValue) throws Exception {

        Field field = aClass.getDeclaredField(entityFields.name());

        if (!filters.isEmpty()) {
            filters.add(" AND ");
        }

        filters.add(field.getName() + " = " + "'" +newValue + "'");

        return this;
    }

    @Override
    public BusinessCentralRecord<T,E> SetFilter(E entityFields, String sqlExpression, Object... newValue) throws Exception {

        Field field = aClass.getDeclaredField(entityFields.name());

        if (BusinessCentralUtils.CountPlaceHolders(sqlExpression) != newValue.length) throw new Exception("the count of parameters does not match the count of placeholders");

        if (!filters.isEmpty()) {
            filters.add(" AND ");
        }

        BusinessCentralUtils.ParserSQLExpression(this.filters,sqlExpression,field.getName(), Arrays.stream(newValue).toArray(Object[]::new));

        return this;
    }

    @Override
    public BusinessCentralRecord<T,E> SetLoadFields(E entityFields) throws Exception {

        Field field = aClass.getDeclaredField(entityFields.name());

        this.loadfields.add(field.getName());

        return this;
    }

    @Override
    public BusinessCentralRecord<T,E> SetLoadFields(E... entityFields) throws Exception {

        for (Enum<E> field : entityFields) {

            Field finalField = this.aClass.getDeclaredField(field.name());

            this.loadfields.add(finalField.getName());
        }
        return this;
    }

    @Override
    public Boolean IsEmpty() {
        return mapper.IsEmpty(String.join(", ", loadfields),filters, tableName) != 0;
    }

    @Override
    public Boolean HasNext() {
        return currentIndex < entityList.size();
    }

    @Override
    public BusinessCentralRecord<T,E> Next() {
        entity = entityList.get(currentIndex);
        currentIndex++;
        return this;
    }

    @Override
    public List<T> FindSet() throws IllegalAccessException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.UpperCamelCaseStrategy());

        List<LinkedHashMap<String, Object>> resultLists = mapper.FindSet(String.join(", ", loadfields), filters, tableName);

        for (LinkedHashMap<String, Object> linkedHashMap : resultLists) {
            this.entityList.add(objectMapper.convertValue(linkedHashMap, aClass));
        }

        return this.entityList;
    }


    @Override
    public T FindFirst() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {

        LinkedHashMap<String, Object> result = mapper.FindFirst(String.join(", ", loadfields), filters,tableName);

        ConvertToObject(result);

        return this.entity;
    }

    @Override
    public T FindLast() throws IllegalAccessException {

        LinkedHashMap<String, Object> result = mapper.FindLast(String.join(", ", loadfields), filters, tableName);

        ConvertToObject(result);

        return this.entity;
    }

    @Override
    public List<T> Find(Integer Count) {
        return (List<T>) mapper.Find(String.join(", ", loadfields), filters, Count, tableName);
    }


    @Override
    public T Get(Object ID) throws IllegalAccessException {
        this.filters.clear();
        this.filters.add(primaryKey.getName() + " = " + "'" + ID + "'");

        LinkedHashMap<String, Object> result = mapper.Get(String.join(", ", loadfields), filters, tableName);

        ConvertToObject(result);

        return this.entity;
    }

    @Override
    public Integer Count() throws Exception {

        if (loadfields.size() > 1) throw new Exception("There are more than one fields within Count expression!");

        return mapper.Count(String.join(", ", loadfields),filters,tableName);
    }

    @Override
    public T GetRecord() {
        return this.entity;
    }

    @Override
    public BusinessCentralRecord<T, E> SetRecord(Object record) {
        entity = (T) record;
        BeanUtils.copyProperties(entity,x_entity);
        return this;
    }

    @Override
    public BusinessCentralRecord<T,E> Reset() {
        this.filters.clear();
        this.loadfields.clear();
        this.currentIndex = 0;
        return this;
    }

    @Override
    public BusinessCentralRecord<T,E> Init() {

        Method[] methods = this.aClass.getDeclaredMethods();

        for (Method method : methods) {

            if (method.isAnnotationPresent(OnInit.class)) {

                method.setAccessible(true);

                T bean = applicationContext.getBean(this.aClass);

                this.entity = (T) ReflectionUtils.invokeMethod(method, bean);

                break;
            }
        }

        return this;
    }


    @Override
    public BusinessCentralRecord<T,E> SetCurrentKey() {
        return null;
    }

    @Override
    public BusinessCentralRecord<T,E> Validate(E entityFields, Object newValue, Boolean TriggerEvent) throws Exception {

        Field field = this.aClass.getDeclaredField(entityFields.name());

        if (!TriggerEvent) {

            field.setAccessible(true);

            field.set(entity,newValue);
        }

        if (TriggerEvent) {

            if (!field.isAnnotationPresent(TableField.class)) return this;

            String onValidate = field.getAnnotation(TableField.class).ON_VALIDATE();

            if (onValidate.isBlank()) {

                field.setAccessible(true);

                field.set(entity,newValue);

                return this;
            }

            Method method = ReflectionUtils.findMethod(this.aClass, onValidate, Object.class, this.aClass);

            if (method != null) {

                method.setAccessible(true);

                T bean = applicationContext.getBean(this.aClass);

                this.entity = (T) ReflectionUtils.invokeMethod(method, bean,newValue,entity);
            }
        }

        return this;
    }

    @Override
    @Transactional
    public Boolean Delete() throws NoSuchFieldException, IllegalAccessException {

        Method[] methods = this.aClass.getDeclaredMethods();

        for (Method method : methods) {

            if (method.isAnnotationPresent(OnDelete.class)) {

                method.setAccessible(true);

                T bean = applicationContext.getBean(this.aClass);

                this.entity = (T) ReflectionUtils.invokeMethod(method, bean,this.entity);

                break;
            }
        }

        Field field = this.entity.getClass().getDeclaredField(primaryKey.getName());

        field.setAccessible(true);

        this.keyValue = field.get(this.entity);

        System.out.println(this.keyValue.getClass());

        if (this.keyValue.getClass().isAssignableFrom(String.class)) {
            this.keyValue = "'" + this.keyValue + "'";
        }

        return mapper.Delete(primaryKey.getName(),keyValue.toString(),tableName) != 0;
    }

    @Override
    public Boolean Modify(Boolean UseEvent) throws Exception {

        Method[] methods = this.aClass.getDeclaredMethods();

        for (Method method : methods) {

            if (method.isAnnotationPresent(OnModify.class)) {

                method.setAccessible(true);

                T bean = applicationContext.getBean(this.aClass);

                this.entity = (T) ReflectionUtils.invokeMethod(method, bean,this.entity);

                break;
            }
        }


        Map<String, Object> diffMap = BusinessCentralUtils.compareObjects(entity, x_entity);

        Field field = this.entity.getClass().getDeclaredField(primaryKey.getName());

        field.setAccessible(true);

        this.keyValue = field.get(this.entity);

        if (diffMap.isEmpty()) return true;

        return mapper.Modify(diffMap,primaryKey.getName(),keyValue,tableName) != 0;
    }

    @Override
    public Boolean Insert(Boolean UseEvent, Boolean FullFields) {

        Method[] methods = this.aClass.getDeclaredMethods();

        for (Method method : methods) {

            if (method.isAnnotationPresent(OnInsert.class)) {

                method.setAccessible(true);

                T bean = applicationContext.getBean(this.aClass);

                this.entity = (T) ReflectionUtils.invokeMethod(method, bean,this.entity);

                break;
            }
        }

        List<String> fieldNameList = BusinessCentralUtils.getFieldNameList(this.entity,true);
        List<Object> valueList = BusinessCentralUtils.getFieldValueList(this.entity,true);

        Integer count;

        if (FullFields) {
            count = mapper.InsertWithFullField(fieldNameList,valueList,tableName);
        }else {
            count = mapper.Insert(fieldNameList,valueList,tableName);
        }

        return count != 0;
    }

    @Override
    public BusinessCentralRecord<T,E> SetCurrentRecord(T entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public BusinessCentralRecord<T, E> SetXRecord(T X_entity) {
        BeanUtils.copyProperties(X_entity,this.x_entity);
        return this;
    }

    private void ConvertToObject(LinkedHashMap<String, Object> properties) throws IllegalAccessException {

        List<Field> fields = Arrays.stream(this.entity.getClass().getDeclaredFields()).filter(field -> !field.isAnnotationPresent(Autowired.class)).toList();

        for (Field field : fields) {

            for (Map.Entry<String, Object> stringObjectEntry : properties.entrySet()) {

                if (field.getName().equals(stringObjectEntry.getKey())) {

                    if (!ObjectUtils.isEmpty(stringObjectEntry.getValue())) {

                        field.setAccessible(true);

                        if (Enum.class.isAssignableFrom(field.getType())) {
                            Class<?> enumType = field.getType();
                            Object enumValue = Enum.valueOf((Class<Enum>) enumType, stringObjectEntry.getValue().toString());
                            field.set(this.entity, enumValue);
                        }else {
                            field.set(this.entity,stringObjectEntry.getValue());
                        }
                    }
                }
            }
        }

        BeanUtils.copyProperties(this.entity,this.x_entity);
    }

}
