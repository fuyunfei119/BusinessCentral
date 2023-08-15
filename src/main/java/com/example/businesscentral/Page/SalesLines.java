package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Enum.Sales_Order_Status_Enum;
import com.example.businesscentral.Table.Customer;
import com.example.businesscentral.Table.SalesOrder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Page(SOURCETABLE = "SalesOrder", TYPE = PageType.List)
@Data
public class SalesLines {

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private System System_ID;

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private String Customer_ID;

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private Date Order_Date;

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private Double Total_Amount;

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private Sales_Order_Status_Enum Order_Status;

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private String Item_ID;

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private String Product_Name;

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private Integer Quantity;

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private Double Price;

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
    public SalesOrder OnBeforeOnAfterGetRecord(BusinessCentralRecord<SalesOrder,SalesOrder.Fields> Rec) throws Exception {
//        System.out.println("Page => OnAfterGetRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnNextRecord
    public Integer OnBeforeOnNextRecord(Integer Steps) {
//        System.out.println("Page => OnNextRecord Trigger Raised...");
        return Steps;
    }

    @OnAfterGetCurrRecord
    public SalesOrder OnBeforeOnAfterCurrRecRecord(BusinessCentralRecord<SalesOrder,SalesOrder.Fields> Rec) throws Exception {
//        System.out.println("Page => OnAfterGetCurrRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnNewRecord
    public SalesOrder OnBeforeNewRecord(BusinessCentralRecord<SalesOrder,SalesOrder.Fields> Rec) throws Exception {
//        System.out.println("Page => OnNewRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnInsertRecord
    public SalesOrder OnBeforeInsertRecord(BusinessCentralRecord<SalesOrder,SalesOrder.Fields> Rec) throws Exception {
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
}
