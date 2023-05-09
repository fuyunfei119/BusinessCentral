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

    public static class OnBeforeCheckIfHasOver_250_PointCustomers extends CustomerEvent {
        private Boolean IsHandled;


        public OnBeforeCheckIfHasOver_250_PointCustomers(Object source,Boolean isHandled) {
            super(source);
            this.IsHandled = isHandled;
        }

        public Boolean getHandled() {
            return IsHandled;
        }
    }

    public static class OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers extends CustomerEvent {
        private Boolean IsHandled;

        public OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(Object source, Boolean isHandled) {
            super(source);
            IsHandled = isHandled;
        }

        public Boolean getHandled() {
            return IsHandled;
        }
    }
}
