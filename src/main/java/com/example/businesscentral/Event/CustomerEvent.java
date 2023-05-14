package com.example.businesscentral.Event;

import org.springframework.context.ApplicationEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CustomerEvent extends ApplicationEvent {

    public CustomerEvent(Object source) {
        super(source);
    }

    public static class OnBeforeCheckIfCustomerHasOverDueBalance extends CustomerEvent {
        private AtomicBoolean IsHandled;

        public OnBeforeCheckIfCustomerHasOverDueBalance(Object source, AtomicBoolean IsHandled) {
            super(source);
            this.IsHandled = IsHandled;
        }

        public AtomicBoolean getHandled() {
            return IsHandled;
        }

        public void setHandled(AtomicBoolean handled) {
            IsHandled = IsHandled;
        }
    }

    public static class OnBeforeCheckIfHasOver_250_PointCustomers extends CustomerEvent {
        private AtomicBoolean IsHandled;

        public OnBeforeCheckIfHasOver_250_PointCustomers(Object source,AtomicBoolean IsHandled) {
            super(source);
            this.IsHandled = IsHandled;
        }

        public AtomicBoolean getHandled() {
            return IsHandled;
        }

        public void setHandled(boolean IsHandled) {
            this.IsHandled.set(IsHandled);
        }
    }

    public static class OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers extends CustomerEvent {
        private AtomicBoolean IsHandled;

        public OnBeforeReturnResultOnAfterCheckIfHasOver_250_PointCustomers(Object source, AtomicBoolean IsHandled) {
            super(source);
            IsHandled = IsHandled;
        }

        public AtomicBoolean getHandled() {
            return IsHandled;
        }

        public void setHandled(AtomicBoolean handled) {
            IsHandled = IsHandled;
        }
    }

    public static class OnBeforeInsertNewCustomer extends CustomerEvent {
        private AtomicBoolean IsHandled;

        public OnBeforeInsertNewCustomer(Object source, AtomicBoolean isHandled) {
            super(source);
            IsHandled = isHandled;
        }

        public AtomicBoolean getHandled() {
            return IsHandled;
        }

        public void setHandled(AtomicBoolean handled) {
            IsHandled = IsHandled;
        }
    }
}
