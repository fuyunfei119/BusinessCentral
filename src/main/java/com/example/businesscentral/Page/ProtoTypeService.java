package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.BusinessCentralProtoType;
import com.example.businesscentral.Dao.BusinessCentralProtoTypeQuery;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ProtoTypeService {

    @Autowired
    private BusinessCentralProtoType<Customer.Fields> CUSTOMER;
    @Autowired
    private BusinessCentralProtoTypeQuery protoTypeQuery;

    public List<LinkedHashMap<String, Object>> FindSetProtoType() throws Exception {
        CUSTOMER.SetPrimaryKey(Customer.Fields.userId);
        CUSTOMER.SetLoadFields(Customer.Fields.userId);
        CUSTOMER.SetLoadFields(Customer.Fields.firstName);
        CUSTOMER.SetLoadFields(Customer.Fields.lastName);
         return CUSTOMER.FindSet();
    }

    public List<LinkedHashMap<String, Object>> FindSetByTableName(String TableName) throws ClassNotFoundException {
        return protoTypeQuery.FindSetByTableName(TableName);
    }


}
