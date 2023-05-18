package com.example.businesscentral.Table;

import com.example.businesscentral.Annotation.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.UUID;

@FieldNameConstants(asEnum = true)
@Data
@OnInit("OnInitTriggerMethod")
@OnInsert("OnInsertTriggerMethod")
@OnDelete("OnDeleteTriggerMethod")
@OnModify("OnModifyTriggerMethod")
@Table
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
    private Date lastUpdatedTime;

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

    @Trigger
    private Customer OnInitTriggerMethod() {
        this.userId = UUID.randomUUID().toString();
        this.accountStatus = "Active";
        this.accountCreationDate = new Date(System.currentTimeMillis());
        return this;
    }

    @Trigger
    private void OnInsertTriggerMethod() {
        System.out.println("Insert Trigger Raised...");
    }

    @Trigger
    private void OnModifyTriggerMethod() {
        System.out.println("Modify Trigger Raised...");
    }

    @Trigger
    private void OnDeleteTriggerMethod() {
        System.out.println("Delete Trigger Raised...");
    }

    @Trigger
    private Customer OnValidatePhoneNumberTriggerMethod(Object newValue) {
        if (StringUtils.hasLength(newValue.toString())) {
            this.phoneNumber = newValue.toString();
        }

        return this;
    }

}
