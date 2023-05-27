package com.example.businesscentral.Dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Scope("prototype")
public interface BusinessCentralPage<T,E extends Enum<E>> {

    BusinessCentralPage<T,E> SetLoadFields(E field) throws NoSuchFieldException;

    BusinessCentralPage<T,E> SetRange(E field, Object Value) throws NoSuchFieldException;

    BusinessCentralPage<T,E> SetFilter(E field, String sqlExpression, Object... Value);

    List<LinkedHashMap<String,Object>> FindSet();

    List<LinkedHashMap<String,Object>> FindFirst(Boolean Prototype);

    LinkedHashMap<String, Object> FindLast(Boolean Prototype);

    List<LinkedHashMap<String, Object>> Find(Integer Count,Boolean Prototype);

    LinkedHashMap<String, Object> Get(Object ID, Boolean Prototype);
}
