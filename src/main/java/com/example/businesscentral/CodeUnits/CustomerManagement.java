package com.example.businesscentral.CodeUnits;

import com.example.businesscentral.Dao.Annotation.CodeUnit;
import com.example.businesscentral.Dao.RecordData.CustomerRecord;
import com.example.businesscentral.Event.CustomerEvent;
import com.example.businesscentral.Table.customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@CodeUnit
public class CustomerManagement {

    @Autowired
    private CustomerRecord customerRecord;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public List<customer> test() throws IllegalAccessException {
        return customerRecord.FindSet();
    }

    public List<customer> InsertNewCustomer() throws Exception {

        List<customer> customers = new ArrayList<>();

        customerRecord.Init();
        customerRecord.Validate(customer.Fields.First_Name,"YUNFEI",false);
        customerRecord.Validate(customer.Fields.Last_Name,"FU",false);
        customerRecord.Validate(customer.Fields.Phone_Number,"123456789",true);
        Boolean inserted = customerRecord.Insert(true, true);

        if (inserted) {
            customerRecord.Reset();
            customerRecord.SetRange(customer.Fields.First_Name,"YUNFEI");
            customerRecord.SetRange(customer.Fields.Last_Name,"FU");
            customerRecord.FindFirst();
        }

        customerRecord.Validate(customer.Fields.Email_Address,"fuyunfei119@gmail.com",false);
        customerRecord.Modify(true);
        customer customer = customerRecord.GetRecord();
        customers.add(customer);

        return customers;
    }

    public List<customer> CheckIfHasOver_PointsCustomers() throws Exception {

        AtomicBoolean IsHandled = new AtomicBoolean(false);
        OnBeforeCheckIfHasOver_250_PointCustomers(IsHandled);
        if (IsHandled.get()) return null;

        customerRecord.Reset();
        customerRecord.SetLoadFields(customer.Fields.First_Name);
        customerRecord.SetLoadFields(customer.Fields.Email_Address);
        customerRecord.SetLoadFields(customer.Fields.Account_Status);
        customerRecord.SetLoadFields(customer.Fields.Billing_Address);
        customerRecord.SetLoadFields(customer.Fields.Points);
        customerRecord.SetLoadFields(customer.Fields.Email_Address);
        customerRecord.SetLoadFields(customer.Fields.Account_Creation_Date);
        customerRecord.SetFilter(customer.Fields.Points,">%1&<%2",50,500);
        customerRecord.SetRange(customer.Fields.Account_Status,"Active");
        customerRecord.SetFilter(customer.Fields.First_Name,"%1*","J");
        List<customer> customers = customerRecord.FindSet();

        OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(customers,IsHandled);
        return !IsHandled.get() ? customers : null;
    }

    private void OnBeforeCheckIfHasOver_250_PointCustomers(AtomicBoolean IsHandled) {
        this.applicationEventPublisher.publishEvent(new CustomerEvent.OnBeforeCheckIfHasOver_250_PointCustomers(new Object(),IsHandled));
    }

    private void OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(List<customer> customers, AtomicBoolean IsHandled) {
        this.applicationEventPublisher.publishEvent(new CustomerEvent.OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(customers,IsHandled));
    }

    private void OnBeforeInsertNewCustomer(AtomicBoolean IsHandled) {
        this.applicationEventPublisher.publishEvent(new CustomerEvent.OnBeforeInsertNewCustomer(new Object(),IsHandled));
    }
}
