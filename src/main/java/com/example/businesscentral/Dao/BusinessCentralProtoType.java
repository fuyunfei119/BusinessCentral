package com.example.businesscentral.Dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Scope("prototype")
public interface BusinessCentralProtoType<T extends Enum<T>> {

    BusinessCentralProtoType<T> SetPrimaryKey(T field) throws Exception;

    BusinessCentralProtoType<T> SetLoadFields(T field) throws NoSuchFieldException;

    BusinessCentralProtoType<T> SetRange(T field,Object Value) throws NoSuchFieldException;

    BusinessCentralProtoType<T> SetFilter(T field,String sqlExpression, Object... Value);

    List<LinkedHashMap<String,Object>> FindSet();
}
