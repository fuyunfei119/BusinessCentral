package com.example.businesscentral.Dao.Impl;

import com.example.businesscentral.Annotation.OnInit;
import com.example.businesscentral.Annotation.OnValidate;
import com.example.businesscentral.Dao.BusinessCentral;
import com.example.businesscentral.Dao.Mapper.BusinessCentralMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class BusinessCentralMySql<T,E extends Enum<E>> implements BusinessCentral<T,E> {

    @Autowired
    private BusinessCentralMapper mapper;
    @Autowired
    private ApplicationContext applicationContext;
    private List<LinkedHashMap<String,Object>> entityList;
    private List<LinkedHashMap<String,Object>> x_entityList;
    private final List<String> filters = new ArrayList<>();
    private final List<String> loadfilters = new ArrayList<>();
    private Field primaryKey;
    private List<T> classList;
    private T entity;
    private T x_entity;
    private Class<T> aClass;
    private Object keyValue;
    private Class fields;
    private String finalfields;

    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public BusinessCentral<T,E> SetSource(Class<T> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.aClass = tClass;
        this.primaryKey = BusinessCentralUtils.getPrimaryKeyField(this.aClass);
        this.entity = this.aClass.getDeclaredConstructor().newInstance();
        this.x_entity = this.aClass.getDeclaredConstructor().newInstance();
        return this;
    }

    @Override
    public BusinessCentral<T,E> SetRange(E entityFields, Object newValue) throws Exception {

        Field field = aClass.getDeclaredField(entityFields.name());

        if (!filters.isEmpty()) {
            filters.add(" AND ");
        }

        filters.add(BusinessCentralUtils.convertToSnakeCase(field.getName()) + " = " + "'" +newValue + "'");

        return this;
    }

    @Override
    public BusinessCentral<T,E> SetFilter(E entityFields, String sqlExpression, Object... newValue) throws Exception {

        Field field = aClass.getDeclaredField(entityFields.name());

        if (BusinessCentralUtils.CountPlaceHolders(sqlExpression) != newValue.length) throw new Exception("the count of parameters does not match the count of placeholders");

        if (!filters.isEmpty()) {
            filters.add(" AND ");
        }

        BusinessCentralUtils.ParserSQLExpression(this.filters,sqlExpression,field.getName(), Arrays.stream(newValue).toArray(Object[]::new));

        return this;
    }

    @Override
    public BusinessCentral<T,E> SetLoadFields(E entityFields) throws Exception {
        Field field = aClass.getDeclaredField(entityFields.name());

        this.loadfilters.add(BusinessCentralUtils.convertToSnakeCase(field.getName()));

        return this;
    }

    @Override
    public BusinessCentral<T,E> SetLoadFields(E... entityFields) throws Exception {

        for (Enum<E> field : entityFields) {
            Field finalField = this.aClass.getDeclaredField(field.name());

            this.loadfilters.add(BusinessCentralUtils.convertToSnakeCase(finalField.getName()));
        }
        return this;
    }

    @Override
    public Boolean IsEmpty() {
        return mapper.IsEmpty(
                String.join(", ", loadfilters),filters) != 0;
    }

    @Override
    public List<LinkedHashMap<String,Object>> FindSet(Boolean UpdateRequired,Boolean Prototype){
        this.entityList = mapper.FindSet(String.join(", ", loadfilters), filters);
        return entityList;
    }

    @Override
    public List<T> FindSet(Boolean UpdateRequired) {
        this.classList = (List<T>) mapper.FindSet(String.join(", ", loadfilters), filters);
        return this.classList;
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindFirst(Boolean Prototype) {
        return mapper.FindFirst(String.join(", ", loadfilters), filters);
    }

    @Override
    public List<T> FindFirst() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {

//        System.out.println("this entity => "+this.entity);

        List<LinkedHashMap<String, Object>> linkedHashMaps = mapper.FindFirst(String.join(", ", loadfilters), filters);

        for (Field field : this.entity.getClass().getDeclaredFields()) {

            for (Map.Entry<String, Object> stringObjectEntry : linkedHashMaps.get(0).entrySet()) {

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

//        List<T> list = (List<T>) mapper.FindFirst(String.join(", ", loadfilters), filters);
//        this.entity = list.get(0);
//        BeanUtils.copyProperties(this.entity,this.x_entity);
        return null;
    }

    @Override
    public LinkedHashMap<String, Object> FindLast(Boolean Prototype) {
        return mapper.FindLast(String.join(", ", loadfilters),filters);
    }

    @Override
    public List<T> FindLast() {
        return (List) mapper.FindLast(String.join(", ", loadfilters),filters);
    }

    @Override
    public List<LinkedHashMap<String, Object>> Find(Integer Count,Boolean Prototype) {
        return mapper.Find(String.join(", ", loadfilters), filters, Count);
    }

    @Override
    public List<T> Find(Integer Count) {
        return (List<T>) mapper.Find(String.join(", ", loadfilters), filters, Count);
    }

    @Override
    public LinkedHashMap<String, Object> Get(Object ID, Boolean Prototype) {
        return mapper.Get(String.join(", ", loadfilters),filters);
    }

    @Override
    public T Get(Object ID) {
        this.filters.clear();
        this.filters.add(BusinessCentralUtils.convertToSnakeCase(primaryKey.getName() + " = " + "'" + ID + "'"));

        return (T) mapper.Get(String.join(", ", loadfilters),filters);
    }

    @Override
    public Integer Count() throws Exception {

        if (loadfilters.size() > 1) throw new Exception("There are more than one fields within Count expression!");

        return mapper.Count(String.join(", ", loadfilters),filters);
    }

    @Override
    public T GetRecord() {
        return this.entity;
    }

    @Override
    public BusinessCentral<T,E> Reset() {
        this.filters.clear();
        this.loadfilters.clear();
        return this;
    }

    @Override
    public BusinessCentral<T,E> Init() {

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
    public BusinessCentral<T,E> SetCurrentKey() {
        return null;
    }

    @Override
    public BusinessCentral<T,E> Validate(E entityFields, Object newValue, Boolean TriggerEvent) throws Exception {

        Field field = this.aClass.getDeclaredField(entityFields.name());

        if (!TriggerEvent) {

//            System.out.println("Validate => "+ this.entity);

            field.setAccessible(true);

            field.set(entity,newValue);
        }

        if (TriggerEvent) {

            if (!field.isAnnotationPresent(OnValidate.class)) return this;

            OnValidate onValidate = Objects.requireNonNull(field.getAnnotation(OnValidate.class));

            if (onValidate.value().isBlank()) return this;

            String methodName = onValidate.value();

            Method method = ReflectionUtils.findMethod(this.aClass, methodName, Object.class);

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

        System.out.println("OnBeforeDelete => " + this.entity);

        Field field = this.entity.getClass().getDeclaredField(primaryKey.getName());

        field.setAccessible(true);

        this.keyValue = field.get(this.entity);

        System.out.println(this.keyValue);

        return mapper.Delete(BusinessCentralUtils.convertToSnakeCase(primaryKey.getName()),this.keyValue) != 0;
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
}
