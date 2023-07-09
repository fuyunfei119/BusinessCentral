package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.Enum.PageType;

@Page(SOURCETABLE = "Customer", TYPE = PageType.List, Method = "FindSetProtoType")
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

    @OnAfterGetRecord
    public void OnBeforeOnAfterGetRecord() {
        System.out.println("OnAfterGetRecord trigger raised...");
    }

    @OnFindRecord
    public void OnBeforeOnFindRecord() {
        System.out.println("OnFindRecord trigger raised...");
    }

    @OnNextRecord
    public void OnBeforeOnNextRecord() {
        System.out.println("OnNextRecord trigger raised...");
    }

    @OnAfterGetCurrRecord
    public void OnBeforeOnAfterCurrRecRecord() {
        System.out.println("OnAfterCurrRecRecord trigger raised...");
    }

    @OnQueryClosePage
    public void OnBeforeOnQueryClosePage() {
        System.out.println("OnQueryClosePage trigger raised...");
    }

    @OnClosePage
    public void OnClosePage() {
        System.out.println("OnClosePage trigger raised...");
    }
}
