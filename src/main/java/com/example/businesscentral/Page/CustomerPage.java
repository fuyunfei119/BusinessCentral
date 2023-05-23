package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.PageData.CustomerPageData;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;

@Page
public class CustomerPage {

    @Autowired
    private CustomerPageData CUSTOMER;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    public List<LinkedHashMap<String, Object>> FindSetProtoType() throws Exception {
        CUSTOMER.SetPrimaryKey(Customer.Fields.userId);
        CUSTOMER.SetLoadFields(Customer.Fields.userId);
        CUSTOMER.SetLoadFields(Customer.Fields.firstName);
        CUSTOMER.SetLoadFields(Customer.Fields.lastName);
         return CUSTOMER.FindSet();
    }

    public List<LinkedHashMap<String, Object>> FindSetByTableName(String TableName) throws ClassNotFoundException {
        return businessCentralSystemRecord.FindSetByTableName(TableName);
    }


}
