package com.example.businesscentral.Table;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.BusinessCentralBase;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.UUID;

@Table
@Data
@FieldNameConstants(asEnum = true)
public class Customer extends BusinessCentralBase {

    @Keys(PRIMARY_KEY = true,AUTO_INCREMENT = true)
    @TableField(
            INIT_VALUE = "",
            NOT_BLANK = false
    )
    private String userId;

    @TableField(
                ON_VALIDATE = "",
                FLOW_FIELD = @FlowField()
    )
    private String firstName;

    private String lastName;

    private String emailAddress;

    @TableField(
            ON_VALIDATE = "OnValidatePhoneNumberTriggerMethod"
    )
    private String phoneNumber;

    private String billingAddress;

    private String shippingAddress;

    private java.sql.Date accountCreationDate;
    
    private java.sql.Date lastLoginDate;

    private Date lastUpdatedTime;
    
    private String accountStatus;

    private String paymentInformation;

    private String orderHistory;

    private Integer Points;

    private String customerType;

    @OnInit
    private Customer OnInitTriggerMethod() {
        System.out.println("Init Trigger Raised...");

        this.userId = UUID.randomUUID().toString();
        this.accountStatus = "Active";
        this.accountCreationDate = new Date(System.currentTimeMillis());
        return this;
    }

    @OnInsert
    private Customer OnInsertTriggerMethod() {
        System.out.println("Insert Trigger Raised...");
        return this;
    }

    @OnModify
    private Customer OnModifyTriggerMethod() {
        System.out.println("Modify Trigger Raised...");
        return this;
    }

    @OnDelete
    private Customer OnDeleteTriggerMethod() {
        System.out.println("Delete Trigger Raised...");
        return this;
    }

    private Customer OnValidatePhoneNumberTriggerMethod(Object newValue) {
        System.out.println("Validate Trigger Raised...");

        if (StringUtils.hasLength(newValue.toString())) {
            this.phoneNumber = newValue.toString();
        }

        return this;
    }

}
