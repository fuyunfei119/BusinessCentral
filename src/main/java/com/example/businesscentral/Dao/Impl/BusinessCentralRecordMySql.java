package com.example.businesscentral.Dao.Impl;

import com.example.businesscentral.Dao.Annotation.OnInit;
import com.example.businesscentral.Dao.Annotation.TableField;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Mapper.BusinessCentralMapper;
import com.example.businesscentral.Dao.Mapper.BusinessCentralProtoTypeMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
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
    private BusinessCentralMapper<T> mapper;
    @Autowired
    private BusinessCentralProtoTypeMapper businessCentralProtoTypeMapper;
    @Autowired
    private ApplicationContext applicationContext;
    private final List<String> filters = new ArrayList<>();
    private final List<String> loadfields = new ArrayList<>();
    private Field primaryKey;
    private Object keyValue;
    private List<T> classList;
    private T entity;
    private T x_entity;
    private Class<T> aClass;

    @Override
    public BusinessCentralRecord<T,E> SetSource(Class<T> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.aClass = tClass;
        this.primaryKey = BusinessCentralUtils.getPrimaryKeyField(this.aClass);
        this.entity = this.aClass.getDeclaredConstructor().newInstance();
        this.x_entity = this.aClass.getDeclaredConstructor().newInstance();
        return this;
    }

    @Override
    public BusinessCentralRecord<T,E> SetRange(E entityFields, Object newValue) throws Exception {

        Field field = aClass.getDeclaredField(entityFields.name());

        if (!filters.isEmpty()) {
            filters.add(" AND ");
        }

        filters.add(BusinessCentralUtils.convertToSnakeCase(field.getName()) + " = " + "'" +newValue + "'");

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

        this.loadfields.add(BusinessCentralUtils.convertToSnakeCase(field.getName()));

        return this;
    }

    @Override
    public BusinessCentralRecord<T,E> SetLoadFields(E... entityFields) throws Exception {

        for (Enum<E> field : entityFields) {
            Field finalField = this.aClass.getDeclaredField(field.name());

            this.loadfields.add(BusinessCentralUtils.convertToSnakeCase(finalField.getName()));
        }
        return this;
    }

    @Override
    public Boolean IsEmpty() {
        return mapper.IsEmpty(String.join(", ", loadfields),filters) != 0;
    }


    @Override
    public List<T> FindSet() {
        System.out.println("Result => " + mapper.FindSet(String.join(", ", loadfields), filters));

        return this.classList;
    }


    @Override
    public T FindFirst() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {

        return mapper.FindFirst(String.join(", ", loadfields), filters);
    }

    @Override
    public T FindLast() {
        return mapper.FindLast(String.join(", ", loadfields),filters);
    }

    @Override
    public List<T> Find(Integer Count) {
        return mapper.Find(String.join(", ", loadfields), filters, Count);
    }


    @Override
    public T Get(Object ID) {
        this.filters.clear();
        this.filters.add(BusinessCentralUtils.convertToSnakeCase(primaryKey.getName() + " = " + "'" + ID + "'"));

        return mapper.Get(String.join(", ", loadfields),filters);
    }

    @Override
    public Integer Count() throws Exception {

        if (loadfields.size() > 1) throw new Exception("There are more than one fields within Count expression!");

        return mapper.Count(String.join(", ", loadfields),filters);
    }

    @Override
    public T GetRecord() {
        return this.entity;
    }

    @Override
    public BusinessCentralRecord<T,E> Reset() {
        this.filters.clear();
        this.loadfields.clear();
        return this;
    }

    @Override
    public BusinessCentralRecord<T,E> Init() {

        if (!this.aClass.isAnnotationPresent(OnInit.class)) return this;

        OnInit onInit = Objects.requireNonNull(this.aClass.getDeclaredAnnotation(OnInit.class));

        if (onInit.value().isBlank()) return this;

        String methodName = onInit.value();

        Method method = ReflectionUtils.findMethod(this.aClass, methodName);

        if (method != null) {

            method.setAccessible(true);

            Object bean = applicationContext.getBean(this.aClass);

            this.entity = (T) ReflectionUtils.invokeMethod(method, bean);
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

            String onValidate = Objects.requireNonNull(field.getAnnotation(TableField.class)).ON_VALIDATE();

            if (onValidate.isBlank()) return this;

            Method method = ReflectionUtils.findMethod(this.aClass, onValidate, Object.class);

            if (method != null) {

                method.setAccessible(true);

                Object bean = applicationContext.getBean(this.aClass);

                this.entity = (T) ReflectionUtils.invokeMethod(method, bean,newValue);
            }
        }

        return this;
    }

    @Override
    public Boolean Delete() throws NoSuchFieldException, IllegalAccessException {

        Field field = this.entity.getClass().getDeclaredField(primaryKey.getName());

        field.setAccessible(true);

        this.keyValue = field.get(this.entity);

        return mapper.Delete(BusinessCentralUtils.convertToSnakeCase(primaryKey.getName()),this.keyValue.toString()) != 0;
    }

    @Override
    public Boolean Modify(Boolean UseEvent) throws Exception {

        Map<String, Object> diffMap = BusinessCentralUtils.compareObjects(entity, x_entity);

        Field field = this.entity.getClass().getDeclaredField(primaryKey.getName());

        field.setAccessible(true);

        this.keyValue = field.get(this.entity);

        return mapper.Modify(diffMap,BusinessCentralUtils.convertToSnakeCase(primaryKey.getName()),keyValue) != 0;
    }

    @Override
    public Boolean Insert(Boolean UseEvent, Boolean FullFields) {

        List<String> fieldNameList = BusinessCentralUtils.getFieldNameList(this.entity,true);
        List<Object> valueList = BusinessCentralUtils.getFieldValueList(this.entity,true);

        Integer count;

        if (FullFields) {
            count = mapper.InsertWithFullField(fieldNameList,valueList);
        }else {
            count = mapper.Insert(fieldNameList,valueList);
        }

//        System.out.println("After Insert => "+this.entity);

        return count != 0;
    }

    private List<T> ConvertToObject(List<LinkedHashMap<String, Object>> properties) throws IllegalAccessException {

        List<T> list = new ArrayList<>();

        for (Field field : this.entity.getClass().getDeclaredFields()) {

            for (Map.Entry<String, Object> stringObjectEntry : properties.get(0).entrySet()) {

                if (field.getName().equals(stringObjectEntry.getKey())) {

                    if (!ObjectUtils.isEmpty(stringObjectEntry.getValue())) {

                        field.setAccessible(true);

                        field.set(this.entity,stringObjectEntry.getValue());
                    }
                }
            }
        }

        System.out.println("After FindFirst => " + this.entity);

        BeanUtils.copyProperties(this.entity,this.x_entity);

        list.add(this.entity);

        return list;

    }
}
