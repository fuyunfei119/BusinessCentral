package com.example.businesscentral.Dao.RecordData;

import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Table.customer;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface CustomerRecord extends BusinessCentralRecord<customer, customer.Fields> {
}
