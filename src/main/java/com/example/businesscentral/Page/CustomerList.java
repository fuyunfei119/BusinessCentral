package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import com.example.businesscentral.Table.Customer;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Page(SOURCETABLE = "Customer", TYPE = PageType.List)
@Data
public class CustomerList {

    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String userId;

    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String firstName;

    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String lastName;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private String customerType;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private Integer points;

    @PageField(
            VISIABLE = true,
            GROUP = "Address"
    )
    private String phoneNumber;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private String accountStatus;

    @OnOpenPage
    public void OnBeforeOnOpenPage() {
        System.out.println("OnOpenPage trigger raised...");
    }

    @OnFindRecord
    public List<LinkedHashMap<String,Object>> OnBeforeOnFindRecord(List<LinkedHashMap<String,Object>> Records) {
        System.out.println("OnFindRecord trigger raised...");
        return Records;
    }

    @OnAfterGetRecord
    public Customer OnBeforeOnAfterGetRecord(Customer customer) {
        System.out.println("OnAfterGetRecord trigger raised...");
        System.out.println(customer);
        return customer;
    }

    @OnNextRecord
    public Integer OnBeforeOnNextRecord(Integer Steps) {
        System.out.println("OnNextRecord trigger raised...");
        Steps = 2;
        return Steps;
    }

    @OnAfterGetCurrRecord
    public void OnBeforeOnAfterCurrRecRecord() { System.out.println("OnAfterCurrRecRecord trigger raised..."); }

    @OnQueryClosePage
    public void OnBeforeOnQueryClosePage() {
        System.out.println("OnQueryClosePage trigger raised...");
    }

    @OnClosePage
    public void OnClosePage() {
        System.out.println("OnClosePage trigger raised...");
    }
}
