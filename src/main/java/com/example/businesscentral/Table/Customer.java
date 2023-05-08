package com.example.businesscentral.Table;

import com.example.businesscentral.Annotation.*;
import com.example.businesscentral.System.*;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants(asEnum = true)
@TableNameConstants
@Entity
@OnInit(InitTriggerType.OnInitEntity)
@OnInsert(InsertTriggerType.OnCheckEntityResultOnBeforeInsert)
@OnDelete(DeleteTriggerType.OnCheckEntityResultOnBeforeDelete)
@OnModify(ModifyTriggerType.OnModifyEntity)
public class Customer {
    @PK
    private String userId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    @OnValidate(ValidateTriggerType.CheckIfAddressIsIIegel)
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;
    private java.sql.Date accountCreationDate;
    private java.sql.Date lastLoginDate;
    private String accountStatus;
    private String paymentInformation;
    private String orderHistory;
    private Integer Points;
    private String customerType;
}
