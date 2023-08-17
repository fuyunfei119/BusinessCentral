package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Enum.Account_Status_Enum;
import com.example.businesscentral.Table.customer;
import lombok.Data;

import java.util.Date;

@Page(SOURCETABLE = "customer", TYPE = PageType.Card)
@Data
public class customerCard {

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
            GROUP = "General"
    )
    private String Email_Address;

    @PageField(
            VISIABLE = true,
            GROUP = "Address"
    )
    private String Phone_Number;

    @PageField(
            VISIABLE = true,
            GROUP = "Address"
    )
    private String Billing_Address;

    @PageField(
            VISIABLE = true,
            GROUP = "Address"
    )
    private String Shipping_Address;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private Date Account_Creation_Date;

    @PageField(
            VISIABLE = true,
            GROUP = "System"
    )
    private Date Last_Login_Date;

    @PageField(
            VISIABLE = true,
            GROUP = "System"
    )
    private java.sql.Timestamp Last_Updated_Time;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private Account_Status_Enum Account_Status;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private String Payment_Information;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private String Order_History;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting",
            ON_VALIDATE = "OnValidatePoints"
    )
    private Integer Points;

    @OnOpenPage
    public void OnBeforeOpenPage() {
//        System.out.println("Card => OnOpenPage Trigger Raised...");
    }

    @OnFindRecord
    public customer OnBeforeOnFindRecord(BusinessCentralRecord<customer, customer.Fields> Rec) {
//        System.out.println("Card => OnFindRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnAfterGetRecord
    public customer OnBeforeOnAfterGetRecord(BusinessCentralRecord<customer, customer.Fields> Rec) throws Exception {
//        System.out.println("Card => OnAfterGetRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnAfterGetCurrRecord
    public customer OnBeforeOnAfterCurrRecRecord(BusinessCentralRecord<customer, customer.Fields> Rec) throws Exception {
//        System.out.println("Card => OnAfterGetCurrRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnNewRecord
    public customer OnBeforeNewRecord(BusinessCentralRecord<customer, customer.Fields> Rec) {
//        System.out.println("Card => OnNewRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnInsertRecord
    public customer OnBeforeInsertRecord(BusinessCentralRecord<customer, customer.Fields> Rec) throws Exception {
//        System.out.println("Card => OnInsertRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnQueryClosePage
    public void OnBeforeOnQueryClosePage(BusinessCentralRecord<customer, customer.Fields> Rec) {
//        System.out.println("Card => OnQueryClosePage Trigger Raised...");
    }

    @OnClosePage
    public void OnClosePage() {
//        System.out.println("Card => OnClosePage Trigger Raised...");
    }

    private customer OnValidatePoints(Object currentValue, Object newValue, BusinessCentralRecord<customer, customer.Fields> Rec) {
//        System.out.println("Page => Point Page Validate Trigger Raised...");
        return Rec.GetRecord();
    }

}
