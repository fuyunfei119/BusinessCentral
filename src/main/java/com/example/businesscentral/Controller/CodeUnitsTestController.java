package com.example.businesscentral.Controller;

import com.example.businesscentral.CodeUnits.CustomerManagement;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CodeUnitsTestController {

    @Autowired
    private CustomerManagement customerManagement;

    @GetMapping("/test1234")
    public void test1234() throws Exception {
        customerManagement.InsertNewCustomer();
    }

    @GetMapping("testcustomer")
    public List<Customer> testcustomer() throws IllegalAccessException {
        return customerManagement.test();
    }

}
