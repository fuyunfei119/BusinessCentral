package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.RecordData.CustomerRecord;
import com.example.businesscentral.Enum.Account_Status_Enum;
import com.example.businesscentral.Enum.Customer_Type_Enum;
import com.example.businesscentral.Table.Customer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.LinkedHashMap;
import java.util.List;

@Page(SOURCETABLE = "Customer", TYPE = PageType.List)
@Data
public class CustomerList {

    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String System_ID;

    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String First_Name;

    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String Last_Name;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private Customer_Type_Enum Customer_Type;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting",
            ON_VALIDATE = "OnValidatePoints"
    )
    private Integer Points;

    @PageField(
            VISIABLE = true,
            GROUP = "Address"
    )
    private String Phone_Number;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private Account_Status_Enum Account_Status;

    @OnOpenPage
    public void OnBeforeOnOpenPage() {
//        System.out.println("Page => OnOpenPage Trigger Raised...");
    }

    @OnFindRecord
    public List<LinkedHashMap<String,Object>> OnBeforeOnFindRecord(List<LinkedHashMap<String,Object>> Records) {
//        System.out.println("Page => OnFindRecord Trigger Raised...");
        return Records;
    }

    @OnAfterGetRecord
    public Customer OnBeforeOnAfterGetRecord(BusinessCentralRecord<Customer,Customer.Fields> Rec) throws Exception {
//        System.out.println("Page => OnAfterGetRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnNextRecord
    public Integer OnBeforeOnNextRecord(Integer Steps) {
//        System.out.println("Page => OnNextRecord Trigger Raised...");
        return Steps;
    }

    @OnAfterGetCurrRecord
    public Customer OnBeforeOnAfterCurrRecRecord(BusinessCentralRecord<Customer,Customer.Fields> Rec) throws Exception {
//        System.out.println("Page => OnAfterGetCurrRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnNewRecord
    public Customer OnBeforeNewRecord(BusinessCentralRecord<Customer,Customer.Fields> Rec) throws Exception {
//        System.out.println("Page => OnNewRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnInsertRecord
    public Customer OnBeforeInsertRecord(BusinessCentralRecord<Customer,Customer.Fields> Rec) throws Exception {
//        System.out.println("Page => OnInsertRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnQueryClosePage
    public void OnBeforeOnQueryClosePage() {
//        System.out.println("Page => ");
    }

    @OnClosePage
    public void OnClosePage() {
    }

    @Action(NAME = "ChangeAccounStatus")
    public void SendMessage() throws Exception {

        record.Reset();
        record.FindSet();
        while (record.HasNext()) {
            record.Next();
            record.Validate(Customer.Fields.Account_Status,"Closed",false);
            record.Modify(true);
        }
    }

    private Customer OnValidatePoints(Object currentValue, Object newValue,BusinessCentralRecord<Customer,Customer.Fields> Rec) {
//        System.out.println("Page => Point Page Validate Trigger Raised...");
        return Rec.GetRecord();
    }

    @Autowired
    private CustomerRecord record;
}
