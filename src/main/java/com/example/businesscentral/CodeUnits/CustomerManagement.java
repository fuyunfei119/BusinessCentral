package com.example.businesscentral.CodeUnits;

import com.example.businesscentral.Dao.Annotation.CodeUnit;
import com.example.businesscentral.Dao.RecordData.CustomerRecord;
import com.example.businesscentral.Event.CustomerEvent;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    public List<Customer> InsertNewCustomer() throws Exception {

        List<Customer> customers = new ArrayList<>();

        customerRecord.Init();
        customerRecord.Validate(Customer.Fields.firstName,"YUNFEI",false);
        customerRecord.Validate(Customer.Fields.lastName,"FU",false);
        customerRecord.Validate(Customer.Fields.phoneNumber,"123456789",true);
        Boolean inserted = customerRecord.Insert(true, true);

        if (inserted) {
            customerRecord.Reset();
            customerRecord.SetRange(Customer.Fields.firstName,"YUNFEI");
            customerRecord.SetRange(Customer.Fields.lastName,"FU");
            customerRecord.FindFirst();
        }

        customerRecord.Validate(Customer.Fields.emailAddress,"fuyunfei119@gmail.com",false);
        customerRecord.Modify(true);
        Customer customer = customerRecord.GetRecord();
        customers.add(customer);

        System.out.println("Result => " + customerRecord.Delete());

        return customers;
    }

    public List<Customer> CheckIfHasOver_PointsCustomers() throws Exception {

        AtomicBoolean IsHandled = new AtomicBoolean(false);
        OnBeforeCheckIfHasOver_250_PointCustomers(IsHandled);
        if (IsHandled.get()) return null;

        customerRecord.Reset();
        customerRecord.SetLoadFields(Customer.Fields.firstName);
        customerRecord.SetLoadFields(Customer.Fields.emailAddress);
        customerRecord.SetLoadFields(Customer.Fields.accountStatus);
        customerRecord.SetLoadFields(Customer.Fields.billingAddress);
        customerRecord.SetLoadFields(Customer.Fields.points);
        customerRecord.SetLoadFields(Customer.Fields.emailAddress);
        customerRecord.SetLoadFields(Customer.Fields.accountCreationDate);
        customerRecord.SetFilter(Customer.Fields.points,">%1&<%2",50,500);
        customerRecord.SetRange(Customer.Fields.accountStatus,"Active");
        customerRecord.SetFilter(Customer.Fields.firstName,"%1*","J");
        List<Customer> customers = customerRecord.FindSet();

        System.out.println(customers);

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
