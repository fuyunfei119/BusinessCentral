package com.example.businesscentral.Dao;

import com.example.businesscentral.Dao.Request.ActionParamter;
import com.example.businesscentral.Dao.Request.CardGroup;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Scope("prototype")
public interface BusinessCentralSystemRecord {

    List<LinkedHashMap<String,Object>> FindSetByTableName(String TableName) throws ClassNotFoundException;

    List<Object> FindSetByFields(Map<String,Object> filters);

    List<LinkedHashMap<String, Object>> FindSetByFilters(Map<String, Object> filters) throws Exception;

    List<LinkedHashMap<String, Object>> QueryContent(Map<String,Object> filters);

    List<LinkedHashMap<String, Object>> SortLinesByDescending(Map<String, Object> filters) throws Exception;

    LinkedHashMap<String, Object> GetRecordById(Map<String, Object> filters);

    LinkedHashMap<String,Object> InsertNewRecord(Map<String,Object> objectMap) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    List<String> GetFilterGroups(Map<String, Object> filter);

    List<LinkedHashMap<String,Object>> GetDataForListPage(String table);

    List<String> GetPageActions(ActionParamter listName);

    void RaiseAction(ActionParamter paramter) throws InvocationTargetException, IllegalAccessException;

}
