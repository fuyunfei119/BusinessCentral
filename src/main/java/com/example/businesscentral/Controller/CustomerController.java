package com.example.businesscentral.Controller;

import com.example.businesscentral.CodeUnits.CustomerManagement;
import com.example.businesscentral.Page.CustomerPage;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerManagement customerManagement;
    @Autowired
    private CustomerPage customerPage;

    @GetMapping("/test123")
    public List<Customer> test1() throws Exception { return customerManagement.CheckIfHasOver_PointsCustomers(); }

    @GetMapping("/testnewcustomer")
    public List<Customer> test() throws Exception { return customerManagement.InsertNewCustomer(); }

//    @GetMapping("/FindSet")
//    public List<LinkedHashMap<String,Object>> test2() throws Exception { return customerPage.FindSetProtoType(); }

    @GetMapping("/")
    public List<LinkedHashMap<String, Object>> test3(@RequestParam("table") String tableName) throws ClassNotFoundException { return customerPage.FindSetByTableName(tableName); }

    @GetMapping("/test")
    public String testhelloworld() {
        return "Hello World";
    }
}
