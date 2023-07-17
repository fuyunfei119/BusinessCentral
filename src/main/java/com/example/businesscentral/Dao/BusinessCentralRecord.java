package com.example.businesscentral.Dao;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.Serializable;
import java.util.List;

public interface BusinessCentralRecord<T,E extends Enum<E>> {

    BusinessCentralRecord<T,E> SetRange(E entityFields, Object newValue) throws Exception;

    BusinessCentralRecord<T,E> SetFilter(E entityFields, String sqlExpression, Object... newValue) throws Exception;

    BusinessCentralRecord<T,E> SetLoadFields(E entityFields) throws Exception;

    BusinessCentralRecord<T,E> SetLoadFields(E... entityFields) throws Exception;

    Boolean IsEmpty();

    Boolean HasNext();

    BusinessCentralRecord<T, E> Next();

    List<T> FindSet() throws IllegalAccessException;

    T FindFirst() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException;

    T FindLast() throws IllegalAccessException;

    List<T> Find(Integer Count);

    T Get(Object ID) throws IllegalAccessException;

    Integer Count() throws Exception;

    T GetRecord();

    BusinessCentralRecord<T,E> SetRecord(Object record);

    BusinessCentralRecord<T,E> Reset();

    BusinessCentralRecord<T,E> Init();

    BusinessCentralRecord<T,E> SetCurrentKey();

    BusinessCentralRecord<T,E> Validate(E entityFields, Object newValue, Boolean TriggerEvent) throws Exception;

    Boolean Delete() throws NoSuchFieldException, IllegalAccessException;

    Boolean Modify(Boolean UseEvent) throws Exception;

    Boolean Insert(Boolean UseEvent,Boolean FullFields);
}
