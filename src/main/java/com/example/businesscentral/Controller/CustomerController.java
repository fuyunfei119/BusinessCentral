package com.example.businesscentral.Controller;

import com.example.businesscentral.CodeUnits.CustomerManagement;
import com.example.businesscentral.Dao.Aop.CardAop;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Request.SortParameter;
import com.example.businesscentral.Page.CustomerCard;
import com.example.businesscentral.Page.CustomerPage;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    private CustomerManagement customerManagement;
    @Autowired
    private CustomerPage customerPage;
    @Autowired
    private CustomerCard customerCard;
    @Autowired
    private CardAop cardAop;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    @GetMapping("/test123")
    public List<Customer> test1() throws Exception { return customerManagement.CheckIfHasOver_PointsCustomers(); }

    @GetMapping("/testnewcustomer")
    public List<Customer> test() throws Exception { return customerManagement.InsertNewCustomer(); }

    @GetMapping("/FindSet")
    public List<LinkedHashMap<String,Object>> test2() throws Exception { return customerPage.FindSetProtoType(); }

    @GetMapping("/")
    public List<LinkedHashMap<String, Object>> test3(@RequestParam("table") String tableName) throws ClassNotFoundException { return customerPage.FindSetByTableName(tableName); }

    @PostMapping("/FindSetByFilters")
    public List<LinkedHashMap<String, Object>> FindSetByFilters(@RequestBody Map<String,Object> filters) throws Exception { return customerPage.FindSetByFilters(filters); }

    @PostMapping("/getfilterOptions")
    public List<Object> handleRequest(@RequestBody Map<String,Object> filters) { return customerPage.FindSetByFields(filters); }

    @PostMapping("/getfilterGroups")
    public List<String> getFilterGroup(@RequestBody Map<String,Object> filter) {
        return customerPage.GetFilterGroups(filter);
    }

    @PostMapping("/FetchSearchQuery")
    public List<LinkedHashMap<String, Object>> FetchSearchQuery(@RequestBody Map<String,Object> filters) {
        return customerPage.QueryContent(filters);
    }

    @PostMapping("/SortLines")
    public List<LinkedHashMap<String,Object>> SortByAscending(@RequestBody Map<String, Object> filters) throws Exception {
        return customerPage.SortLinesByDescending(filters);
    }

    @PostMapping("/GetRecordById")
    public List<?> GetRecordById(@RequestBody Map<String, Object> filters) throws Exception {
        return null;
    }

    @PostMapping("/InitNewRecord")
    public List<CardGroup> InitNewRecord(@RequestBody Map<String,String> table) {
        return businessCentralSystemRecord.GetAllFieldNames(table);
    }

    @GetMapping("/test")
    public String testhelloworld() {
        return "Hello World";
    }
}
