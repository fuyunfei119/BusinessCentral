package com.example.businesscentral.Dao.PageData;

import com.example.businesscentral.Dao.BusinessCentralPage;
import com.example.businesscentral.Table.customer;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface CustomerPageData extends BusinessCentralPage<customer, customer.Fields> {
}
