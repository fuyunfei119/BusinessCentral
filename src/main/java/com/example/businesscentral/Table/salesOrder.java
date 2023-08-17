package com.example.businesscentral.Table;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Enum.Sales_Order_Status_Enum;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Date;
import java.util.UUID;

@Table
@Data
@FieldNameConstants(asEnum = true)
public class salesOrder {

    @Keys(PRIMARY_KEY = true,AUTO_INCREMENT = true)
    @TableField(

    )
    private String System_ID;

    @TableField(

    )
    private String Customer_ID;

    @TableField(

    )
    private Date Order_Date;

    @TableField(

    )
    private Double Total_Amount;

    @TableField(

    )
    private Sales_Order_Status_Enum Order_Status;

    @TableField(

    )
    private String Item_ID;

    @TableField(

    )
    private String Product_Name;

    @TableField(

    )
    private Integer Quantity;

    @TableField(

    )
    private Double Price;

    @OnInit
    private salesOrder OnInitTriggerMethod() {
//        System.out.println("Table => Init Trigger Raised...");

        this.System_ID = UUID.randomUUID().toString();
        return this;
    }

    @OnInsert
    private salesOrder OnInsertTriggerMethod(salesOrder Rec) {
//        System.out.println("Table => Insert Trigger Raised...");
        return Rec;
    }

    @OnModify
    private salesOrder OnModifyTriggerMethod(salesOrder Rec) {
//        System.out.println("Table => Modify Trigger Raised...");
        return Rec;
    }

    @OnDelete
    private salesOrder OnDeleteTriggerMethod(salesOrder Rec) {
//        System.out.println("Table => Delete Trigger Raised...");
        return Rec;
    }
}
