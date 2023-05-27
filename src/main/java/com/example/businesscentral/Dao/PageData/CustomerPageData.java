package com.example.businesscentral.Dao.PageData;

import com.example.businesscentral.Dao.BusinessCentralPage;
import com.example.businesscentral.Table.Customer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPageData extends BusinessCentralPage<Customer,Customer.Fields> {
}
