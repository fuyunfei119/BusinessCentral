package com.example.businesscentral.Controller;

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
    public Object onBeforeListMounted(@RequestBody TableParameter table) {
        return null;
    }

    @GetMapping("/List/OnMounted")
    public List<LinkedHashMap<String,Object>> OnListMounted(@RequestParam("list") String list) throws Exception {return null;}

    @PostMapping("/List/OnBeforeUpdate")
    public List<LinkedHashMap<String,Object>> OnBeforeListUpdate(@RequestBody TableParameter table) {
        return null;
    }

    @PostMapping("/List/OnUpdated")
    public LinkedHashMap<String,Object> OnListUpdated(@RequestBody TableParameter table) {
        return null;
    }

    @PostMapping("/List/OnBeforeUnmount")
    public List<LinkedHashMap<String,Object>> OnBeforeListUnmount(@RequestBody TableParameter table) {
        return null;
    }

    @PostMapping("/Card/OnBeforeMounted")
    public Object onBeforeCardMounted(@RequestBody TableParameter table) {
        return null;
    }

    @GetMapping("/Card/OnMounted")
    public List<LinkedHashMap<String,Object>> OnCardMounted(@RequestParam("list") String list) throws Exception {return null;}

    @PostMapping("/Card/OnBeforeUpdate")
    public List<LinkedHashMap<String,Object>> OnBeforeCardUpdate(@RequestBody TableParameter table) {
        return null;
    }

    @PostMapping("/Card/OnUpdated")
    public LinkedHashMap<String,Object> OnCardUpdated(@RequestBody TableParameter table) {
        return null;
    }

    @PostMapping("/Card/OnBeforeUnmount")
    public List<LinkedHashMap<String,Object>> OnBeforeCardUnmount(@RequestBody TableParameter table) {
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
    public List<?> GetRecordById(@RequestBody CardPageID cardPageID) throws Exception {
        return null;
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

    @PostMapping("/List/GetTable")
    public String GetTable(@RequestBody PageParameter pageName) throws Exception {
        return businessCentralSystemRecord.GetTable(pageName);
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

    @PostMapping("/Card/GetFieldOptionForCard")
    public Object GetFieldOptionForCard(@RequestBody CardFieldOption cardFieldOption) {
        return null;
    }

    @GetMapping("/test")
    public String testhelloworld() {
        return "Hello World";
    }
}
