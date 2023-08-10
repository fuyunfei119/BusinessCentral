package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Enum.Account_Status_Enum;
import lombok.Data;

@Page(SOURCETABLE = "Customer", TYPE = PageType.Card)
@Data
public class CustomerCard {

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
    private java.sql.Date Account_Creation_Date;

    @PageField(
            VISIABLE = true,
            GROUP = "System"
    )
    private java.sql.Date Last_Login_Date;

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
            GROUP = "Accounting"
    )
    private Integer Points;

    @OnOpenPage
    public void OnBeforeOpenPage() {
        System.out.println("Card => OnOpenPage Trigger Raised...");
    }
}
