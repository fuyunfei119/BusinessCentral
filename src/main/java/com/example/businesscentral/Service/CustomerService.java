package com.example.businesscentral.Service;

import com.example.businesscentral.Dao.BusinessCentral;
import com.example.businesscentral.Event.CustomerEvent;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CustomerService {

    @Autowired
    private BusinessCentral<Customer,Customer.Fields> CUSTOMER;
    @Autowired
    private BusinessCentral<Customer,Customer.Fields> NEWCUSTOMER;
    @Autowired
    private BusinessCentral<Customer,Customer.Fields> NEWCUSTOMER2;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public List<Customer> InsertNewCustomer() throws Exception {

        List<Customer> customers = new ArrayList<>();

        NEWCUSTOMER.SetSource(Customer.class);
        NEWCUSTOMER.Init();
        NEWCUSTOMER.Validate(Customer.Fields.firstName,"YUNFEI",false);
        NEWCUSTOMER.Validate(Customer.Fields.lastName,"FU",false);
        NEWCUSTOMER.Validate(Customer.Fields.phoneNumber,"123456789",true);
        Boolean inserted = NEWCUSTOMER.Insert(true, true);

        if (inserted) {
            NEWCUSTOMER.Reset();
            NEWCUSTOMER.SetRange(Customer.Fields.firstName,"YUNFEI");
            NEWCUSTOMER.SetRange(Customer.Fields.lastName,"FU");
            NEWCUSTOMER.FindFirst();
        }

        NEWCUSTOMER.Validate(Customer.Fields.emailAddress,"fuyunfei119@gmail.com",false);
        NEWCUSTOMER.Modify(true);
        Customer customer = NEWCUSTOMER.GetRecord();
        customers.add(customer);

        System.out.println("Result => " + NEWCUSTOMER.Delete());

        return customers;
    }

    public List<Customer> CheckIfHasOver_PointsCustomers() throws Exception {

        AtomicBoolean IsHandled = new AtomicBoolean(false);
        OnBeforeCheckIfHasOver_250_PointCustomers(IsHandled);
        if (IsHandled.get()) return null;

        CUSTOMER.SetSource(Customer.class);
        CUSTOMER.SetLoadFields(Customer.Fields.firstName);
        CUSTOMER.SetLoadFields(Customer.Fields.emailAddress);
        CUSTOMER.SetLoadFields(Customer.Fields.accountStatus);
        CUSTOMER.SetLoadFields(Customer.Fields.billingAddress);
        CUSTOMER.SetLoadFields(Customer.Fields.Points);
        CUSTOMER.SetLoadFields(Customer.Fields.emailAddress);
        CUSTOMER.SetFilter(Customer.Fields.Points,">%1&<%2",50,500);
        CUSTOMER.SetRange(Customer.Fields.accountStatus,"Active");
        CUSTOMER.SetFilter(Customer.Fields.firstName,"%1*","J");
        List<Customer> customers = CUSTOMER.FindSet(true);

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
