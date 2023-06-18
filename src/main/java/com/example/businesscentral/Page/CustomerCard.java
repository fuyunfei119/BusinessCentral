package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.PageData.CustomerPageData;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Page
public class CustomerCard {

    @Autowired
    private CustomerPageData customerPageData;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;

    public List<CardGroup> SetCardData() {
        List<CardGroup> cardGroups = new ArrayList<>();
        cardGroups.add(
                new CardGroup()
                        .setGroupName("General")
                        .setFields(GetGeneralFields())
        )

        return
    }

    public LinkedHashMap<String, Object> GetGeneralFields(Map<String, Object> filters) throws Exception {
        customerPageData.SetLoadFields(Customer.Fields.userId);
        customerPageData.SetLoadFields(Customer.Fields.firstName);
        customerPageData.SetLoadFields(Customer.Fields.lastName);
        customerPageData.SetLoadFields(Customer.Fields.customerType);
        customerPageData.SetLoadFields(Customer.Fields.points);
        customerPageData.SetLoadFields(Customer.Fields.phoneNumber);
        customerPageData.SetLoadFields(Customer.Fields.accountStatus);
        return businessCentralSystemRecord.GetRecordById(filters);
    }

}
