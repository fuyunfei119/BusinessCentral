package com.example.businesscentral.Table;

import com.example.businesscentral.Dao.Annotation.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.UUID;

@Table
@Data
@FieldNameConstants(asEnum = true)
public class Customer{

    @Keys(PRIMARY_KEY = true,AUTO_INCREMENT = true)
    @TableField(
            INIT_VALUE = "",
            NOT_BLANK = false
    )
    private String User_ID;

    @TableField(
            ON_VALIDATE = "",
            FLOW_FIELD = @FlowField()
    )
    private String First_Name;

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

    private String Billing_Address;

    private String Shipping_Address;

    private java.sql.Date Account_Creation_Date;

    private java.sql.Date Last_Login_Date;

    private java.sql.Timestamp Last_Updated_Time;

    private String Account_Status;

    private String Payment_Information;

    private String Order_History;

    @TableField(
            ON_VALIDATE = "OnValidatePoint"
    )
    private Integer Points;

    @TableField(
            ON_VALIDATE = "OnValidateCustomerType"
    )
    private String Customer_Type;

    @OnInit
    private Customer OnInitTriggerMethod() {
        System.out.println("Init Trigger Raised...");

        this.User_ID = UUID.randomUUID().toString();
        this.Account_Status = "Active";
        this.Account_Creation_Date = new Date(System.currentTimeMillis());
        return this;
    }

    @OnInsert
    private Customer OnInsertTriggerMethod() {
        System.out.println("Insert Trigger Raised...");
        return this;
    }

    @OnModify
    private Customer OnModifyTriggerMethod(Customer Rec) {
        System.out.println("Modify Trigger Raised...");
        return Rec;
    }

    @OnDelete
    private Customer OnDeleteTriggerMethod() {
        System.out.println("Delete Trigger Raised...");
        return this;
    }

    private Customer OnValidatePhoneNumberTriggerMethod(Object newValue, Customer Rec) {
        System.out.println("Validate Trigger Raised...");

        if (StringUtils.hasLength(newValue.toString())) {
            Rec.setPhone_Number(newValue.toString());
        }

        return Rec;
    }

    private Customer OnValidateCustomerType(Object newValue,Customer Rec) {
        System.out.println("Validate Trigger Raised...");

        if (StringUtils.hasLength(newValue.toString())) {
            Rec.setCustomer_Type(newValue.toString());
        }

        return Rec;
    }

    private Customer OnValidatePoint(Object currentValue, Object newValue,Customer Rec) {
        System.out.println("Point Validate Trigger Raised...");

        if (currentValue != newValue) {
            Rec.setPoints(Integer.valueOf(newValue.toString()));
        }
        return Rec;
    }
}
