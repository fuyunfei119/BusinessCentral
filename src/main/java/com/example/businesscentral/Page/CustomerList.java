package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.RecordData.CustomerRecord;
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
    private String User_ID;

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
    private String Customer_Type;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
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
    private String Account_Status;

    @OnOpenPage
    public void OnBeforeOnOpenPage() {
    }

    @OnFindRecord
    public List<LinkedHashMap<String,Object>> OnBeforeOnFindRecord(List<LinkedHashMap<String,Object>> Records) {
        return Records;
    }

    @OnAfterGetRecord
    public Customer OnBeforeOnAfterGetRecord(BusinessCentralRecord<Customer,Customer.Fields> Rec) throws Exception {
        Rec.Validate(Customer.Fields.Phone_Number,"Irina",true);
        Rec.Modify(true);
        return Rec.GetRecord();
    }

    @OnNextRecord
    public Integer OnBeforeOnNextRecord(Integer Steps) {
//        Steps = 2;
        return Steps;
    }

    @OnAfterGetCurrRecord
    public Customer OnBeforeOnAfterCurrRecRecord(BusinessCentralRecord<Customer,Customer.Fields> Rec) throws Exception {
        Rec.Validate(Customer.Fields.Phone_Number,"********",true);
        Rec.Modify(true);
        return Rec.GetRecord();
    }

    @OnQueryClosePage
    public void OnBeforeOnQueryClosePage() {
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

    @Autowired
    private CustomerRecord record;
}
