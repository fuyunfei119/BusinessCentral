package com.example.businesscentral;

import com.example.businesscentral.Dao.Mapper.BusinessCentralGeneric;
import com.example.businesscentral.Table.customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BusinessCentralRecordApplicationTests {

    @Autowired
    private BusinessCentralGeneric<customer> businessCentralGeneric;

    @Test
    void contextLoads() {
    }

    @Test
    void GenericTest() {
        List<customer> customers = businessCentralGeneric.FindSet();
    }

}
