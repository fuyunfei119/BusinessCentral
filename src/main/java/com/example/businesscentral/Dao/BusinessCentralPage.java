package com.example.businesscentral.Dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Scope("prototype")
public interface BusinessCentralPage<T,E extends Enum<E>> {

    BusinessCentralPage<T,E> SetTable(String tableName);

    BusinessCentralPage<T,E> Reset();

    BusinessCentralPage<T,E> SetLoadFields(E field) throws NoSuchFieldException;

    BusinessCentralPage<T,E> SetLoadFields(String field);

    BusinessCentralPage<T,E> SetRange(E field, Object Value) throws NoSuchFieldException;

    BusinessCentralPage<T,E> SetFilter(E field, String sqlExpression, Object... Value);

    List<LinkedHashMap<String,Object>> FindSet();

    List<LinkedHashMap<String,Object>> FindFirst();

    LinkedHashMap<String, Object> FindLast();

    List<LinkedHashMap<String, Object>> Find(Integer Count);

    LinkedHashMap<String, Object> Get(Object ID);
}
