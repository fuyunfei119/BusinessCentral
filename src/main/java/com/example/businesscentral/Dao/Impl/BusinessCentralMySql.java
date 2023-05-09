package com.example.businesscentral.Dao.Impl;

import com.example.businesscentral.Dao.BusinessCentral;
import com.example.businesscentral.Dao.Mapper.BusinessCentralMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

@Repository
@Scope("prototype")
public class BusinessCentralMySql<T,E extends Enum<E>> implements BusinessCentral<T,E> {

    @Autowired
    private BusinessCentralMapper mapper;
    private List<LinkedHashMap<String,Object>> entityList;
    private List<LinkedHashMap<String,Object>> x_entityList;
    private LinkedHashMap<String,Object> entity;
    private LinkedHashMap<String,Object> x_entity;
    private final List<String> filters = new ArrayList<>();
    private final List<String> loadfilters = new ArrayList<>();
    private Field primaryKey;
    private List<T> classList;
    private Class<?> aClass;
    private Object keyValue;
    private Class fields;
    private String finalfields;

    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public BusinessCentral<T,E> SetSource(Class<T> tClass) {
        this.aClass = tClass;
        this.primaryKey = BusinessCentralUtils.getPrimaryKeyField(this.aClass);
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
    public LinkedHashMap<String, Object> FindFirst(Boolean Prototype) {
        return mapper.FindFirst(String.join(", ", loadfilters), filters);
    }

    @Override
    public List<T> FindFirst() {
        List list = (List) mapper.FindFirst(String.join(", ", loadfilters), filters);
        return list;
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
    public BusinessCentral<T,E> Reset() {
        this.filters.clear();
        this.loadfilters.clear();
        return this;
    }

    @Override
    public BusinessCentral<T,E> Init() {
        return this;
    }

//    @Override
//    public List<T> GetRecord() {
//        return this.entity;
//    }
//
//    @Override
//    public List<T> GetX_Record() {
//        return x_entity;
//    }

    @Override
    public BusinessCentral<T,E> SetCurrentKey() {
        return null;
    }

    @Override
    public BusinessCentral<T,E> Validate(E entityFields, Object newValue, Boolean TriggerEvent) throws Exception {
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
