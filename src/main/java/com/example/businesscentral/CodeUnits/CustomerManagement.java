package com.example.businesscentral.CodeUnits;

import com.example.businesscentral.Dao.Annotation.CodeUnit;
import com.example.businesscentral.Dao.RecordData.CustomerRecord;
import com.example.businesscentral.Event.CustomerEvent;
import com.example.businesscentral.Table.Customer;
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

    public List<Customer> test() throws IllegalAccessException {
        return customerRecord.FindSet();
    }

    public List<Customer> InsertNewCustomer() throws Exception {

        List<Customer> customers = new ArrayList<>();

        customerRecord.Init();
        customerRecord.Validate(Customer.Fields.First_Name,"YUNFEI",false);
        customerRecord.Validate(Customer.Fields.Last_Name,"FU",false);
        customerRecord.Validate(Customer.Fields.Phone_Number,"123456789",true);
        Boolean inserted = customerRecord.Insert(true, true);

        if (inserted) {
            customerRecord.Reset();
            customerRecord.SetRange(Customer.Fields.First_Name,"YUNFEI");
            customerRecord.SetRange(Customer.Fields.Last_Name,"FU");
            customerRecord.FindFirst();
        }

        customerRecord.Validate(Customer.Fields.Email_Address,"fuyunfei119@gmail.com",false);
        customerRecord.Modify(true);
        Customer customer = customerRecord.GetRecord();
        customers.add(customer);

        return customers;
    }

    public List<Customer> CheckIfHasOver_PointsCustomers() throws Exception {

        AtomicBoolean IsHandled = new AtomicBoolean(false);
        OnBeforeCheckIfHasOver_250_PointCustomers(IsHandled);
        if (IsHandled.get()) return null;

        customerRecord.Reset();
        customerRecord.SetLoadFields(Customer.Fields.First_Name);
        customerRecord.SetLoadFields(Customer.Fields.Email_Address);
        customerRecord.SetLoadFields(Customer.Fields.Account_Status);
        customerRecord.SetLoadFields(Customer.Fields.Billing_Address);
        customerRecord.SetLoadFields(Customer.Fields.Points);
        customerRecord.SetLoadFields(Customer.Fields.Email_Address);
        customerRecord.SetLoadFields(Customer.Fields.Account_Creation_Date);
        customerRecord.SetFilter(Customer.Fields.Points,">%1&<%2",50,500);
        customerRecord.SetRange(Customer.Fields.Account_Status,"Active");
        customerRecord.SetFilter(Customer.Fields.First_Name,"%1*","J");
        List<Customer> customers = customerRecord.FindSet();

        OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(customers,IsHandled);
        return !IsHandled.get() ? customers : null;
    }

    private void OnBeforeCheckIfHasOver_250_PointCustomers(AtomicBoolean IsHandled) {
        this.applicationEventPublisher.publishEvent(new CustomerEvent.OnBeforeCheckIfHasOver_250_PointCustomers(new Object(),IsHandled));
    }

    private void OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(List<Customer> customers,AtomicBoolean IsHandled) {
        this.applicationEventPublisher.publishEvent(new CustomerEvent.OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(customers,IsHandled));
    }

    private void OnBeforeInsertNewCustomer(AtomicBoolean IsHandled) {
        this.applicationEventPublisher.publishEvent(new CustomerEvent.OnBeforeInsertNewCustomer(new Object(),IsHandled));
    }
}
