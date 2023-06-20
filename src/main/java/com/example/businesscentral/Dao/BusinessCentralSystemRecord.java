package com.example.businesscentral.Dao;

import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Request.SortParameter;
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

    List<LinkedHashMap<String, Object>> FindSetByFilters(Map<String, Object> filters) throws Exception;

    List<LinkedHashMap<String, Object>> QueryContent(Map<String,Object> filters);

    List<LinkedHashMap<String, Object>> SortLinesByDescending(Map<String, Object> filters) throws Exception;

    LinkedHashMap<String, Object> GetRecordById(Map<String, Object> filters);

    List<CardGroup> GetAllFieldNames(Map<String,String> table);

}
