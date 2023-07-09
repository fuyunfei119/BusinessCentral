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
    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String userId;

    @TableField(
            ON_VALIDATE = "",
            FLOW_FIELD = @FlowField()
    )
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

    @TableField(
            ON_VALIDATE = "",
            INIT_VALUE = "OK",
            FLOW_FIELD = @FlowField(),
            NOT_BLANK = true
    )
    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String emailAddress;

    @TableField(
            ON_VALIDATE = "OnValidatePhoneNumberTriggerMethod"
    )
    @PageField(
            VISIABLE = true,
            GROUP = "Address"
    )
    private String phoneNumber;

    @PageField(
            VISIABLE = true,
            GROUP = "Address"
    )
    private String billingAddress;

    @PageField(
            VISIABLE = true,
            GROUP = "Address"
    )
    private String shippingAddress;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private java.sql.Date accountCreationDate;

    @PageField(
            VISIABLE = true,
            GROUP = "System"
    )
    private java.sql.Date lastLoginDate;

    @PageField(
            VISIABLE = true,
            GROUP = "System"
    )
    private java.sql.Date lastUpdatedTime;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private String accountStatus;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private String paymentInformation;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private String orderHistory;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private Integer points;

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
