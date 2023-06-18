package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.Annotation.TableField;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.PageData.CustomerPageData;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.*;

@Page
public class CustomerCard {

    @Autowired
    private CustomerPageData customerPageData;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;
    @Autowired
    private ApplicationContext applicationContext;

    public List<CardGroup> GetCardData(Map<String, Object> filters) throws Exception {

        List<String> GroupName = new ArrayList<>();
        List<CardGroup> cardGroups = new ArrayList<>();
        Object table = filters.get("table");
        Object recordID = filters.get("RecordID");

        Object bean = applicationContext.getBean(table.toString().toLowerCase(Locale.ROOT));

        for (Field declaredField : bean.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(PageField.class)) {
                PageField annotation = declaredField.getAnnotation(PageField.class);
                if (!annotation.GROUP().isBlank()) {
                    if (!GroupName.contains(annotation.GROUP())) {
                        GroupName.add(annotation.GROUP());
                    }
                }
            }
        }

        cardGroups.add(new CardGroup()
                .setGroupName("General")
                .setFields(GetGeneralFields(table.toString(),recordID.toString())));

        cardGroups.add(new CardGroup()
                .setGroupName("Address")
                .setFields(GetAddressContactFields(table.toString(),recordID.toString())));

        cardGroups.add(new CardGroup()
                .setGroupName("Accounting")
                .setFields(GetAccountingFields(table.toString(),recordID.toString())));

        cardGroups.add(new CardGroup()
                .setGroupName("System")
                .setFields(GetSystemFields(table.toString(),recordID.toString())));

        return cardGroups;
    }

    public LinkedHashMap<String, Object> GetGeneralFields(String table,String recordID) throws Exception {
        customerPageData.Reset();
        customerPageData.SetLoadFields(Customer.Fields.userId);
        customerPageData.SetLoadFields(Customer.Fields.firstName);
        customerPageData.SetLoadFields(Customer.Fields.lastName);
        customerPageData.SetLoadFields(Customer.Fields.emailAddress);
        return customerPageData.Get(recordID,true);
    }

    public LinkedHashMap<String,Object> GetAddressContactFields(String table,String recordID) throws NoSuchFieldException {
        customerPageData.Reset();
        customerPageData.SetLoadFields(Customer.Fields.phoneNumber);
        customerPageData.SetLoadFields(Customer.Fields.billingAddress);
        customerPageData.SetLoadFields(Customer.Fields.shippingAddress);
        return customerPageData.Get(recordID,true);
    }

    public LinkedHashMap<String,Object> GetAccountingFields(String table,String recordID) throws NoSuchFieldException {
        customerPageData.Reset();
        customerPageData.SetLoadFields(Customer.Fields.accountCreationDate);
        customerPageData.SetLoadFields(Customer.Fields.accountStatus);
        customerPageData.SetLoadFields(Customer.Fields.paymentInformation);
        customerPageData.SetLoadFields(Customer.Fields.orderHistory);
        customerPageData.SetLoadFields(Customer.Fields.points);
        return customerPageData.Get(recordID,true);
    }

    public LinkedHashMap<String,Object> GetSystemFields(String table,String recordID) throws NoSuchFieldException {
        customerPageData.Reset();
        customerPageData.SetLoadFields(Customer.Fields.lastLoginDate);
        customerPageData.SetLoadFields(Customer.Fields.lastUpdatedTime);
        return customerPageData.Get(recordID,true);
    }
}
