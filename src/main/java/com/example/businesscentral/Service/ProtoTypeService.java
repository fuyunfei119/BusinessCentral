package com.example.businesscentral.Service;

import com.example.businesscentral.Dao.BusinessCentral;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ProtoTypeService {

    @Autowired
    private BusinessCentral<Customer,Customer.Fields> CUSTOMER;

    public List<LinkedHashMap<String, Object>> FindSetProtoType() {
        return CUSTOMER.FindSet(true,true);
    }

}
