package com.example.businesscentral.Dao;

import java.util.LinkedHashMap;
import java.util.List;

public interface BusinessCentral<T,E extends Enum<E>> {

    BusinessCentral<T,E> SetSource(Class<T> tClass) throws ClassNotFoundException;

    BusinessCentral<T,E> SetRange(E entityFields, Object newValue) throws Exception;

    BusinessCentral<T,E> SetFilter(E entityFields, String sqlExpression, Object... newValue) throws Exception;

    BusinessCentral<T,E> SetLoadFields(E entityFields) throws Exception;

    BusinessCentral<T,E> SetLoadFields(E... entityFields) throws Exception;

    Boolean IsEmpty();

    List<LinkedHashMap<String,Object>> FindSet(Boolean UpdateRequired,Boolean Prototype);

    List<T> FindSet(Boolean UpdateRequired);

    LinkedHashMap<String,Object> FindFirst(Boolean Prototype);

    List<T> FindFirst();

    LinkedHashMap<String, Object> FindLast(Boolean Prototype);

    List<T> FindLast();

    List<LinkedHashMap<String, Object>> Find(Integer Count,Boolean Prototype);

    List<T> Find(Integer Count);

    LinkedHashMap<String, Object> Get(Object ID, Boolean Prototype);

    T Get(Object ID);

    Integer Count() throws Exception;

    BusinessCentral<T,E> Reset();

    BusinessCentral<T,E> Init();

    BusinessCentral<T,E> Init(Boolean TriggerEvent);

//    E GetRecord();
//
//    E GetX_Record();

    BusinessCentral<T,E> SetCurrentKey();

    BusinessCentral<T,E> Validate(E entityFields, Object newValue, Boolean TriggerEvent) throws Exception;

    Boolean Delete();

    Boolean Modify(Boolean UseEvent) throws Exception;

    Boolean Insert(Boolean UseEvent,Boolean FullFields);
}
