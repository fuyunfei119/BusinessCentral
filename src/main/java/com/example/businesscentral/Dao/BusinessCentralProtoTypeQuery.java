package com.example.businesscentral.Dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Scope("prototype")
public interface BusinessCentralProtoTypeQuery {

    List<LinkedHashMap<String,Object>> FindSetByTableName(String TableName) throws ClassNotFoundException;
}
