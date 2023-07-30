package com.example.businesscentral.Controller;

import com.example.businesscentral.CodeUnits.CustomerManagement;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @PostMapping("/List/OnBeforeMounted")
    public List<LinkedHashMap<String,Object>> onBeforeMounted(@RequestBody TableParameter table) {
        return null;
    }

    @GetMapping("/List/OnMounted")
    public List<LinkedHashMap<String,Object>> OnMounted(@RequestParam("list") String list) throws Exception {return null;}

    @PostMapping("/List/OnBeforeUpdate")
    public List<LinkedHashMap<String,Object>> OnBeforeUpdate(@RequestBody TableParameter table) {
        return null;
    }

    @PostMapping("/List/OnUpdated")
    public LinkedHashMap<String,Object> OnUpdated(@RequestBody TableParameter table) {
        return null;
    }

    @PostMapping("/List/OnBeforeUnmount")
    public List<LinkedHashMap<String,Object>> OnBeforeUnmount(@RequestBody TableParameter table) {
        return null;
    }

    @GetMapping("/")
    public List<LinkedHashMap<String, Object>> test3(@RequestParam("table") String tableName) throws ClassNotFoundException {
        return businessCentralSystemRecord.FindSetByTableName(tableName);
    }

    @PostMapping("/FindSetByFilters")
    public List<LinkedHashMap<String, Object>> FindSetByFilters(@RequestBody Map<String,Object> filters) throws Exception {
        return businessCentralSystemRecord.FindSetByFilters(filters);
    }

    @PostMapping("/getfilterOptions")
    public List<Object> handleRequest(@RequestBody Map<String,Object> filters) {
        return businessCentralSystemRecord.FindSetByFields(filters);
    }

    @PostMapping("/getfilterGroups")
    public List<String> getFilterGroup(@RequestBody Map<String,Object> filter) {
        return businessCentralSystemRecord.GetFilterGroups(filter);
    }

    @PostMapping("/FetchSearchQuery")
    public List<LinkedHashMap<String, Object>> FetchSearchQuery(@RequestBody Map<String,Object> filters) {
        return businessCentralSystemRecord.QueryContent(filters);
    }

    @PostMapping("/SortLines")
    public List<LinkedHashMap<String,Object>> SortByAscending(@RequestBody Map<String, Object> filters) throws Exception {
        return businessCentralSystemRecord.SortLinesByDescending(filters);
    }

    @PostMapping("/GetRecordById")
    public List<?> GetRecordById(@RequestBody Map<String, Object> filters) throws Exception {
        return null;
    }

    @PostMapping("/InitNewRecord")
    public List<CardGroup> InitNewRecord(@RequestBody Map<String,String> table) {
        return businessCentralSystemRecord.GetAllFieldNames(table);
    }

    @PostMapping("/InsertOrUpdateRecord")
    public void InsertOrUpdateRecord(@RequestBody Map<String,Object> objectMap) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        businessCentralSystemRecord.InsertNewRecord(objectMap);
    }

    @PostMapping("/GetActions")
    public List<String> GetActions(@RequestBody ActionParamter ListName) {
        return businessCentralSystemRecord.GetPageActions(ListName);
    }

    @PostMapping("/RaiseActions")
    public void RaiseActions(@RequestBody ActionParamter paramter) throws InvocationTargetException, IllegalAccessException {
        businessCentralSystemRecord.RaiseAction(paramter);
    }

    @PostMapping("/List/PageFieldValidate")
    public LinkedHashMap<String, Object> PageFieldValidate(@RequestBody PageValidate pageValidate) { return null; }

    @PostMapping("/List/OnNewRecord")
    public Object OnNewRecord(@RequestBody NewRecord newRecord) {
        return null;
    }

    @PostMapping("/List/OnInsertRecord")
    public Object OnInsertRecord(@RequestBody NewRecord newRecord) {
        return null;
    }

    @PostMapping("/List/DeleteLine")
    public Boolean OnDeleteRecord(@RequestBody DeleteRecord deleteRecord) {
        return null;
    }

    @GetMapping("/test")
    public String testhelloworld() {
        return "Hello World";
    }
}
