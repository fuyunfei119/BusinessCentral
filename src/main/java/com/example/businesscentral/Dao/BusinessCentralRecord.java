package com.example.businesscentral.Dao;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface BusinessCentralRecord<T,E extends Enum<E>> {

    BusinessCentralRecord<T,E> SetSource(Class<T> tClass) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    BusinessCentralRecord<T,E> SetRange(E entityFields, Object newValue) throws Exception;

    BusinessCentralRecord<T,E> SetFilter(E entityFields, String sqlExpression, Object... newValue) throws Exception;

    BusinessCentralRecord<T,E> SetLoadFields(E entityFields) throws Exception;

    BusinessCentralRecord<T,E> SetLoadFields(E... entityFields) throws Exception;

    Boolean IsEmpty();

    List<T> FindSet();

    T FindFirst() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException;

    T FindLast();

    List<T> Find(Integer Count);

    T Get(Object ID);

    Integer Count() throws Exception;

    T GetRecord();

    BusinessCentralRecord<T,E> Reset();

    BusinessCentralRecord<T,E> Init();

    BusinessCentralRecord<T,E> SetCurrentKey();

    BusinessCentralRecord<T,E> Validate(E entityFields, Object newValue, Boolean TriggerEvent) throws Exception;

    Boolean Delete() throws NoSuchFieldException, IllegalAccessException;

    Boolean Modify(Boolean UseEvent) throws Exception;

    Boolean Insert(Boolean UseEvent,Boolean FullFields);
}
