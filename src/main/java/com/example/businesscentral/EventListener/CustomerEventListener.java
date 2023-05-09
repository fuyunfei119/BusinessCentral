package com.example.businesscentral.EventListener;

import com.example.businesscentral.Event.CustomerEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventListener {
    @EventListener
    public void OnBeforeCheckIfCustomerHasOverDueBalance(CustomerEvent.OnBeforeCheckIfCustomerHasOverDueBalance event) {
        System.out.println(event.getSource());
        System.out.println(event.getHandled());
    }

    @EventListener
    public void OnBeforeCheckIfHasOver_250_PointCustomers(CustomerEvent.OnBeforeCheckIfHasOver_250_PointCustomers event) {

    }
}
