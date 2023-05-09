package com.example.businesscentral.Service;

import com.example.businesscentral.Dao.BusinessCentral;
import com.example.businesscentral.Event.CustomerEvent;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private BusinessCentral<Customer,Customer.Fields> CUSTOMER;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public List<Customer> CheckIfHasOver_250_PointsCustomers() throws Exception {

        List<Customer> customers = new ArrayList<>();

        Boolean IsHandled = false;
        OnBeforeCheckIfHasOver_250_PointCustomers(IsHandled);
        if (IsHandled) return null;


        CUSTOMER.SetSource(Customer.class);
        CUSTOMER.SetLoadFields(Customer.Fields.firstName);
        CUSTOMER.SetLoadFields(Customer.Fields.emailAddress);
        CUSTOMER.SetLoadFields(Customer.Fields.accountStatus);
        CUSTOMER.SetLoadFields(Customer.Fields.firstName);
        CUSTOMER.SetLoadFields(Customer.Fields.billingAddress);
        CUSTOMER.SetLoadFields(Customer.Fields.Points);
        CUSTOMER.SetLoadFields(Customer.Fields.emailAddress);
        CUSTOMER.SetFilter(Customer.Fields.Points,">%1&<%2",75,200);
        CUSTOMER.SetRange(Customer.Fields.accountStatus,"Active");
        CUSTOMER.SetFilter(Customer.Fields.firstName,"%1*","J");
        customers = CUSTOMER.FindSet(true);

        OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(customers,IsHandled);
        return !IsHandled ? customers : null;
    }

    private void OnBeforeCheckIfHasOver_250_PointCustomers(Boolean IsHandled) {
        this.applicationEventPublisher.publishEvent(new CustomerEvent.OnBeforeCheckIfHasOver_250_PointCustomers(new Object(),IsHandled));
    }

    private void OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(List<Customer> customers,Boolean IsHandled) {
        this.applicationEventPublisher.publishEvent(new CustomerEvent.OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(customers,IsHandled));
    }

}
