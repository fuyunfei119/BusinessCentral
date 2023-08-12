package com.example.businesscentral.Table;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.RecordData.CustomerRecord;
import com.example.businesscentral.Enum.Account_Status_Enum;
import com.example.businesscentral.Enum.Customer_Type_Enum;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.UUID;

@Table
@Data
@FieldNameConstants(asEnum = true)
public class Customer{

    @Keys(PRIMARY_KEY = true,AUTO_INCREMENT = true)
    @TableField(

    )
    private String System_ID;

    @TableField(
            ON_VALIDATE = "",
            FLOW_FIELD = @FlowField()
    )
    private String First_Name;

    @TableField(

    )
    private String Last_Name;

    @TableField(
            ON_VALIDATE = "",
            INIT_VALUE = "OK",
            FLOW_FIELD = @FlowField(),
            NOT_BLANK = true
    )
    private String Email_Address;

    @TableField(
            ON_VALIDATE = "OnValidatePhoneNumberTriggerMethod"
    )
    private String Phone_Number;

    @TableField(

    )
    private String Billing_Address;

    @TableField(

    )
    private String Shipping_Address;

    @TableField(

    )
    private java.sql.Date Account_Creation_Date;

    @TableField(

    )
    private java.sql.Date Last_Login_Date;

    @TableField(

    )
    private java.sql.Timestamp Last_Updated_Time;

    @TableField(

    )
    private Account_Status_Enum Account_Status;

    @TableField(

    )
    private String Payment_Information;

    @TableField(

    )
    private String Order_History;

    @TableField(
            ON_VALIDATE = "OnValidatePoint"
    )
    private Integer Points;

    @TableField(
            ON_VALIDATE = "OnValidateCustomerType"
    )
    private Customer_Type_Enum Customer_Type;

    @OnInit
    private Customer OnInitTriggerMethod() {
//        System.out.println("Table => Init Trigger Raised...");

        this.System_ID = UUID.randomUUID().toString();
        this.Account_Status = Account_Status_Enum.Active;
        this.Account_Creation_Date = new Date(System.currentTimeMillis());
        return this;
    }

    @OnInsert
    private Customer OnInsertTriggerMethod(Customer Rec) {
//        System.out.println("Table => Insert Trigger Raised...");
        return Rec;
    }

    @OnModify
    private Customer OnModifyTriggerMethod(Customer Rec) {
//        System.out.println("Table => Modify Trigger Raised...");
        return Rec;
    }

    @OnDelete
    private Customer OnDeleteTriggerMethod(Customer Rec) {
//        System.out.println("Table => Delete Trigger Raised...");
        return Rec;
    }

    private Customer OnValidatePhoneNumberTriggerMethod(Object newValue, Customer Rec) {
        System.out.println("Table => Validate Trigger Raised...");
        return Rec;
    }

    private Customer OnValidateCustomerType(Object newValue,Customer Rec) {
        System.out.println("Table => Validate Trigger Raised...");
        return Rec;
    }

    private Customer OnValidatePoint(Object currentValue, Object newValue, Customer Rec) {
        System.out.println("Table => Point Validate Trigger Raised...");
        Rec.setPoints(Integer.valueOf(newValue.toString()));
        return Rec;
    }

    @Autowired
    private CustomerRecord record;
}
