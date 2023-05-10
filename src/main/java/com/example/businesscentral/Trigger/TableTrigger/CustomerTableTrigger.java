package com.example.businesscentral.Trigger.TableTrigger;

import com.example.businesscentral.Annotation.TableTrigger;
import com.example.businesscentral.Annotation.Trigger;
import com.example.businesscentral.System.SystemTriggerType;
import com.example.businesscentral.Table.Customer;

@TableTrigger(CLASS = Customer.class)
public class CustomerTableTrigger {

    @Trigger(RaisedFor = SystemTriggerType.Insert)
    public void OnCheckEntityResultOnBeforeInsert() {
        System.out.println("Insert Trigger Raised......");
    }

    @Trigger(RaisedFor = SystemTriggerType.Init)
    public void OnInitEntity() {
        System.out.println("Init Trigger Raised......");
    }

    @Trigger(RaisedFor = SystemTriggerType.Modify)
    public void OnModifyEntity() {
        System.out.println("Modify Trigger Raised......");
    }

    @Trigger(RaisedFor = SystemTriggerType.Delete)
    public void OnCheckEntityResultOnBeforeDelete() {
        System.out.println("Delete Trigger Raised......");
    }
}
