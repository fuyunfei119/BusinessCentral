package com.example.businesscentral.Table;

import com.example.businesscentral.Annotation.*;
import com.example.businesscentral.System.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants(asEnum = true)
@TableNameConstants
@Entity
@Data
@OnInit(InitTriggerType.OnInitEntity)
@OnInsert(InsertTriggerType.OnCheckEntityResultOnBeforeInsert)
@OnDelete(DeleteTriggerType.OnCheckEntityResultOnBeforeDelete)
@OnModify(ModifyTriggerType.OnModifyEntity)
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

    @OnValidate(ValidateTriggerType.CheckIfAddressIsIIegel)
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

}
