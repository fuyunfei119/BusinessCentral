package com.example.businesscentral.Trigger.FieldTrigger;

import com.example.businesscentral.Annotation.FieldTrigger;
import com.example.businesscentral.Annotation.Trigger;
import com.example.businesscentral.System.SystemTriggerType;
import com.example.businesscentral.Table.Customer;

@FieldTrigger(tablename = Customer.class)
public class CustomerTrigger {

    @Trigger(RaisedFor = SystemTriggerType.Validate)
    public void CheckIfAddressIsIIegel() {
        System.out.println("Validate Trigger Raised......");
    }

    public void doCheckIfAddressIsIIegel() {}
}
