package com.example.businesscentral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("com.example.businesscentral.Dao.PageData")
public class BusinessCentralApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessCentralApplication.class, args);
    }
}
