package com.example.businesscentral.Dao.Impl;

import com.example.businesscentral.Dao.BusinessCentral;
import com.example.businesscentral.Dao.Mapper.BusinessCentralMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Repository
public class BusinessCentralMySql<E extends Enum<E>> implements BusinessCentral<E> {

    @Autowired
    private BusinessCentralMapper mapper;
    private List<E> entityList;
    private List<E> x_entityList;
    private E entity;
    private E x_entity;
    private final List<String> filters = new ArrayList<>();
    private final List<String> loadfilters = new ArrayList<>();
    private Field primaryKey;
    private Class<E> aClass;
    private Object keyValue;

    @Override
    public BusinessCentral<E> SetSource(Class<E> tClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        this.aClass = tClass;
        this.primaryKey = BusinessCentralUtils.getPrimaryKeyField(this.aClass);

        primaryKey.setAccessible(true);

        this.entity = this.aClass.getDeclaredConstructor().newInstance();
        this.x_entity = this.aClass.getDeclaredConstructor().newInstance();
        return this;
    }

    @Override
    public BusinessCentral<E> SetRange(E entityFields, String newValue) throws Exception {

        Field field = aClass.getDeclaredField(entityFields.name());

        if (!filters.isEmpty()) {
            filters.add(" AND ");
        }

        filters.add(BusinessCentralUtils.convertToSnakeCase(field.getName()) + " = " + "'" +newValue + "'");

        return this;
    }

    @Override
    public BusinessCentral<E> SetFilter(E entityFields, String sqlExpression, String... newValue) throws Exception {

        Field field = aClass.getDeclaredField(entityFields.name());

        if (BusinessCentralUtils.CountPlaceHolders(sqlExpression) != newValue.length) throw new Exception("the count of parameters does not match the count of placeholders");

        if (!filters.isEmpty()) {
            filters.add(" AND ");
        }

        BusinessCentralUtils.ParserSQLExpression(this.filters,sqlExpression,field.getName(), Arrays.toString(newValue));

        return this;
    }

    @Override
    public BusinessCentral<E> SetLoadFields(E entityFields) throws Exception {
        Field field = aClass.getDeclaredField(entityFields.name());

        this.loadfilters.add(BusinessCentralUtils.convertToSnakeCase(field.getName()));

        return this;
    }

    @Override
    public BusinessCentral<E> SetLoadFields(E... entityFields) throws Exception {
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
    public List<LinkedHashMap<String,Object>> FindSet(Boolean Prototype) {
        if (Prototype){
            return mapper.FindSet(String.join(", ", loadfilters), filters);
        } else {
            return  (List) mapper.FindSet(String.join(", ", loadfilters), filters);
        }
    }

    @Override
    public List<E> FindSet() {

        this.entityList = (List) mapper.FindSet(String.join(", ", loadfilters), filters);

        return this.entityList;
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindFirst(Boolean Prototype) {
        if (Prototype) {
            return mapper.FindFirst(String.join(", ", loadfilters), filters);
        } else {
            return (List) mapper.FindFirst(String.join(", ", loadfilters), filters);
        }
    }

    @Override
    public List<E> FindFirst() {
        List list = (List) mapper.FindFirst(String.join(", ", loadfilters), filters);
        return list;
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindLast(Boolean Prototype) {
        if (Prototype) {
            return mapper.FindLast(String.join(", ", loadfilters),filters);
        }else {
            return (List) mapper.FindLast(String.join(", ", loadfilters),filters);
        }
    }

    @Override
    public List<E> FindLast() {
        return (List) mapper.FindLast(String.join(", ", loadfilters),filters);
    }

    @Override
    public List<LinkedHashMap<String, Object>> Find(Integer Count,Boolean Prototype) {
        return mapper.Find(String.join(", ", loadfilters), filters, Count);
    }

    @Override
    public List<E> Find(Integer Count) {
        return (List) mapper.Find(String.join(", ", loadfilters), filters, Count);
    }

    @Override
    public List<LinkedHashMap<String, Object>> Get(Object ID, Boolean Prototype) {
        if (Prototype) {
            return mapper.Get(String.join(", ", loadfilters),filters);
        } else {
            return (List) mapper.Get(String.join(", ", loadfilters),filters);
        }
    }

    @Override
    public List<E> Get(Object ID) {
        this.filters.clear();
        this.filters.add(BusinessCentralUtils.convertToSnakeCase(primaryKey.getName() + " = " + "'" + ID + "'"));

        return (List) mapper.Get(String.join(", ", loadfilters),filters);
    }

    @Override
    public Integer Count() throws Exception {

        if (loadfilters.size() > 1) throw new Exception("There are more than one fields within Count expression!");

        return mapper.Count(String.join(", ", loadfilters),filters);
    }

    @Override
    public BusinessCentral<E> Reset() {
        this.filters.clear();
        this.loadfilters.clear();
        BeanUtils.copyProperties(this.entity,this.x_entity);
        return this;
    }

    @Override
    public BusinessCentral<E> Init() {
        return this;
    }

    @Override
    public E GetRecord() {
        return this.entity;
    }

    @Override
    public E GetX_Record() {
        return x_entity;
    }

    @Override
    public BusinessCentral<E> SetCurrentKey() {
        return null;
    }

    @Override
    public BusinessCentral<E> Validate(E entityFields, Object newValue, Boolean TriggerEvent) throws Exception {
        return this;
    }

    @Override
    public Boolean Delete() {
        return null;
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

        return count != 0;
    }
}
