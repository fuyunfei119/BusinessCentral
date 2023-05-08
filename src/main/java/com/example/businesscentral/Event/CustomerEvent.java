package com.example.businesscentral.Event;

import org.springframework.context.ApplicationEvent;

public class CustomerEvent extends ApplicationEvent {

    public CustomerEvent(Object source) {
        super(source);
    }

    public static class OnBeforeCheckIfCustomerHasOverDueBalance extends CustomerEvent {
        private Boolean IsHandled;

        public OnBeforeCheckIfCustomerHasOverDueBalance(Object source, Boolean isHandled) {
            super(source);
            this.IsHandled = isHandled;
        }

        public Boolean getHandled() {
            return IsHandled;
        }
    }
}
