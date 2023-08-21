package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Enum.Sales_Order_Status_Enum;
import com.example.businesscentral.Table.salesOrder;
import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Page(SOURCETABLE = "salesOrder", TYPE = PageType.List)
@Data
public class salesLines {

    @PageField(
            VISIABLE = true,
            GROUP = ""
    )
    private String System_ID;

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
    public salesOrder OnBeforeOnAfterGetRecord(BusinessCentralRecord<salesOrder, salesOrder.Fields> Rec) throws Exception {
//        System.out.println("Page => OnAfterGetRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnNextRecord
    public Integer OnBeforeOnNextRecord(Integer Steps) {
//        System.out.println("Page => OnNextRecord Trigger Raised...");
        return Steps;
    }

    @OnAfterGetCurrRecord
    public salesOrder OnBeforeOnAfterCurrRecRecord(BusinessCentralRecord<salesOrder, salesOrder.Fields> Rec) throws Exception {
//        System.out.println("Page => OnAfterGetCurrRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnNewRecord
    public salesOrder OnBeforeNewRecord(BusinessCentralRecord<salesOrder, salesOrder.Fields> Rec) throws Exception {
//        System.out.println("Page => OnNewRecord Trigger Raised...");
        return Rec.GetRecord();
    }

    @OnInsertRecord
    public salesOrder OnBeforeInsertRecord(BusinessCentralRecord<salesOrder, salesOrder.Fields> Rec) throws Exception {
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
