package com.example.businesscentral.Dao;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

public interface BusinessCentral<E extends Enum<E>> {

    BusinessCentral<E> SetSource(Class<E> tClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    BusinessCentral<E> SetRange(E entityFields, String newValue) throws Exception;

    BusinessCentral<E> SetFilter(E entityFields, String sqlExpression, String... newValue) throws Exception;

    BusinessCentral<E> SetLoadFields(E entityFields) throws Exception;

    BusinessCentral<E> SetLoadFields(E... entityFields) throws Exception;

    Boolean IsEmpty();

    List<LinkedHashMap<String,Object>> FindSet(Boolean Prototype);

    List<E> FindSet();

    List<LinkedHashMap<String,Object>> FindFirst(Boolean Prototype);

    List<E> FindFirst();

    List<LinkedHashMap<String, Object>> FindLast(Boolean Prototype);

    List<E> FindLast();

    List<LinkedHashMap<String, Object>> Find(Integer Count,Boolean Prototype);

    List<E> Find(Integer Count);

    List<LinkedHashMap<String, Object>> Get(Object ID, Boolean Prototype);

    List<E> Get(Object ID);

    Integer Count() throws Exception;

    BusinessCentral<E> Reset();

    BusinessCentral<E> Init();

    E GetRecord();

    E GetX_Record();

    BusinessCentral<E> SetCurrentKey();

    BusinessCentral<E> Validate(E entityFields, Object newValue, Boolean TriggerEvent) throws Exception;

    Boolean Delete();

    Boolean Modify(Boolean UseEvent) throws Exception;

    Boolean Insert(Boolean UseEvent,Boolean FullFields);
}
