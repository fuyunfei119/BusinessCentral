package com.example.businesscentral.Service;

import com.example.businesscentral.Dao.BusinessCentral;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private BusinessCentral<Customer,Customer.Fields> businessCentral;

    public List<Customer> test1() throws Exception {

        return businessCentral.SetSource(Customer.class)
                .SetLoadFields(Customer.Fields.firstName)
                .SetLoadFields(Customer.Fields.emailAddress)
                .SetLoadFields(Customer.Fields.accountStatus)
                .SetLoadFields(Customer.Fields.firstName)
                .SetLoadFields(Customer.Fields.billingAddress)
                .SetLoadFields(Customer.Fields.Points)
                .SetLoadFields(Customer.Fields.emailAddress)
                .SetFilter(Customer.Fields.Points,">%1&<%2",75,200)
                .SetRange(Customer.Fields.accountStatus,"Active")
                .SetFilter(Customer.Fields.firstName,"%1*","J")
                .FindSet(true);
    }

    public void test2(Enum<Customer.Fields> fields) {
        System.out.println(fields);
    }

}
