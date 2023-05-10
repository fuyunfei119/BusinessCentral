package com.example.businesscentral.EventListener;

import com.example.businesscentral.Event.CustomerEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventListener {
    @EventListener
    public void OnBeforeCheckIfCustomerHasOverDueBalance(CustomerEvent.OnBeforeCheckIfCustomerHasOverDueBalance event) {

    }

    @EventListener
    public void OnBeforeCheckIfHasOver_250_PointCustomers(CustomerEvent.OnBeforeCheckIfHasOver_250_PointCustomers event) {
        System.out.println("OnBeforeCheckIfHasOver_250_PointCustomers event Raised!!!");
        event.setHandled(true);
        System.out.println(event.getHandled());
    }
}
