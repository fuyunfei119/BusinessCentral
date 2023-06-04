package com.example.businesscentral.Dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Scope("prototype")
public interface BusinessCentralSystemRecord {
    List<LinkedHashMap<String,Object>> FindSetByTableName(String TableName) throws ClassNotFoundException;

    List<LinkedHashMap<String, Object>> FindSetByFields(Map<String,Object> filters);

    List<LinkedHashMap<String, Object>> FindSetByFilters(Map<String, Object> filters);
}
