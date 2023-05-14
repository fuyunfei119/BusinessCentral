package com.example.businesscentral.Table;

import com.example.businesscentral.Annotation.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants(asEnum = true)
@TableNameConstants
@Entity
@Data
@OnInit("OnInitTriggerMethod")
@OnInsert("OnInsertTriggerMethod")
@OnDelete("OnDeleteTriggerMethod")
@OnModify("OnModifyTriggerMethod")
public class Customer {
    @PK
    private String userId;

    @FieldCaption(caption = "First Name")
    @ToolTip(value = "Specifies the first name of this customer!")
    @ApplicationArea(All = true)
    private String firstName;

    @ApplicationArea(All = true)
    private String lastName;

    @ApplicationArea(All = true)
    private String emailAddress;

    @OnValidate("OnValidatePhoneNumberTriggerMethod")
    @ApplicationArea(All = true)
    private String phoneNumber;

    @ApplicationArea(All = true)
    private String billingAddress;

    @ApplicationArea(All = true)
    private String shippingAddress;

    @ApplicationArea(All = true)
    private java.sql.Date accountCreationDate;

    @ApplicationArea(All = true)
    private java.sql.Date lastLoginDate;

    @ApplicationArea(All = true)
    private String accountStatus;

    @FlowField
    @ApplicationArea(All = true)
    private String paymentInformation;

    @ApplicationArea(All = true)
    private String orderHistory;

    @ApplicationArea(All = true)
    private Integer Points;

    @ApplicationArea(All = true)
    private String customerType;


    private void OnInitTriggerMethod() { System.out.println("Init Trigger Raised..."); }

    private void OnInsertTriggerMethod() {
        System.out.println("Insert Trigger Raised...");
    }

    private void OnModifyTriggerMethod() {
        System.out.println("Modify Trigger Raised...");
    }

    private void OnDeleteTriggerMethod() {
        System.out.println("Delete Trigger Raised...");
    }

    private void OnValidatePhoneNumberTriggerMethod() {
        System.out.println("Validate Trigger Raised...");
    }

}
