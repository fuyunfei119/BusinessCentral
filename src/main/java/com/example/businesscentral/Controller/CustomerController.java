package com.example.businesscentral.Controller;

import com.example.businesscentral.Service.CustomerService;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping("/test123")
    public List<Customer> test1() throws Exception {
        return service.CheckIfHasOver_PointsCustomers();
    }

    @GetMapping("/testnewcustomer")
    public List<Customer> test() throws Exception {
        return service.InsertNewCustomer();
    }
}
