package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.Annotation.TableField;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.PageData.CustomerPageData;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Table.Customer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.*;

@Page(
        SOURCETABLE = "Customer",
        TYPE = PageType.Card,
        Method = "GetCardData"
)
public class CustomerCard {

    @Autowired
    private CustomerPageData customerPageData;

    public LinkedHashMap<String, Object> GetCardData(String table, String recordID) throws Exception {
        customerPageData.Reset();
        customerPageData.SetLoadFields(Customer.Fields.userId);
        customerPageData.SetLoadFields(Customer.Fields.firstName);
        customerPageData.SetLoadFields(Customer.Fields.lastName);
        customerPageData.SetLoadFields(Customer.Fields.emailAddress);
        customerPageData.SetLoadFields(Customer.Fields.phoneNumber);
        customerPageData.SetLoadFields(Customer.Fields.billingAddress);
        customerPageData.SetLoadFields(Customer.Fields.shippingAddress);
        customerPageData.SetLoadFields(Customer.Fields.accountCreationDate);
        customerPageData.SetLoadFields(Customer.Fields.accountStatus);
        customerPageData.SetLoadFields(Customer.Fields.paymentInformation);
        customerPageData.SetLoadFields(Customer.Fields.orderHistory);
        customerPageData.SetLoadFields(Customer.Fields.points);
        customerPageData.SetLoadFields(Customer.Fields.lastLoginDate);
        customerPageData.SetLoadFields(Customer.Fields.lastUpdatedTime);
        return customerPageData.Get(recordID,true);
    }
}
