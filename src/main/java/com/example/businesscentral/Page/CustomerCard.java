package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.PageData.CustomerPageData;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Page(SOURCETABLE = "Customer", TYPE = PageType.Card, Method = "GetCardData")
public class CustomerCard {

    @Autowired
    private CustomerPageData customerPageData;

    public LinkedHashMap<String, Object> GetCardData(String table, String recordID) throws Exception {
        customerPageData.Reset();
        customerPageData.SetLoadFields(Customer.Fields.User_ID);
        customerPageData.SetLoadFields(Customer.Fields.First_Name);
        customerPageData.SetLoadFields(Customer.Fields.Last_Name);
        customerPageData.SetLoadFields(Customer.Fields.Email_Address);
        customerPageData.SetLoadFields(Customer.Fields.Phone_Number);
        customerPageData.SetLoadFields(Customer.Fields.Billing_Address);
        customerPageData.SetLoadFields(Customer.Fields.Shipping_Address);
        customerPageData.SetLoadFields(Customer.Fields.Account_Creation_Date);
        customerPageData.SetLoadFields(Customer.Fields.Account_Status);
        customerPageData.SetLoadFields(Customer.Fields.Payment_Information);
        customerPageData.SetLoadFields(Customer.Fields.Order_History);
        customerPageData.SetLoadFields(Customer.Fields.Points);
        customerPageData.SetLoadFields(Customer.Fields.Last_Login_Date);
        customerPageData.SetLoadFields(Customer.Fields.Last_Updated_Time);
        return customerPageData.Get(recordID,true);
    }
}
