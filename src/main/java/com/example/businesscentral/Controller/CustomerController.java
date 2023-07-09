package com.example.businesscentral.Controller;

import com.example.businesscentral.CodeUnits.CustomerManagement;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Page.CustomerList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    private CustomerManagement customerManagement;
    @Autowired
    private CustomerList customerList;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

//    @GetMapping("/test123")
//    public List<Customer> test1() throws Exception { return customerManagement.CheckIfHasOver_PointsCustomers(); }
//
//    @GetMapping("/testnewcustomer")
//    public List<Customer> test() throws Exception { return customerManagement.InsertNewCustomer(); }

    @GetMapping("/List")
    public List<LinkedHashMap<String,Object>> FindSetForList(@RequestParam("list") String list) throws Exception {
//        return customerPage.FindSetProtoType();
        return null;
    }

    @GetMapping("/")
    public List<LinkedHashMap<String, Object>> test3(@RequestParam("table") String tableName) throws ClassNotFoundException { return businessCentralSystemRecord.FindSetByTableName(tableName); }

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

    @GetMapping("/test")
    public String testhelloworld() {
        return "Hello World";
    }
}
