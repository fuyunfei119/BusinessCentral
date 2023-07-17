package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.BusinessCentralBase;
import com.example.businesscentral.Dao.BusinessCentralPage;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.Mapper.BusinessCentralProtoTypeMapper;
import com.example.businesscentral.Dao.ProtoType.PageMySql;
import com.example.businesscentral.Dao.RecordData.CustomerRecord;
import com.example.businesscentral.Table.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

@Page(SOURCETABLE = "Customer", TYPE = PageType.List)
@Data
public class CustomerList {

    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String userId;

    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String firstName;

    @PageField(
            VISIABLE = true,
            GROUP = "General"
    )
    private String lastName;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private String customerType;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private Integer points;

    @PageField(
            VISIABLE = true,
            GROUP = "Address"
    )
    private String phoneNumber;

    @PageField(
            VISIABLE = true,
            GROUP = "Accounting"
    )
    private String accountStatus;

    @OnOpenPage
    public void OnBeforeOnOpenPage() {
    }

    @OnFindRecord
    public List<LinkedHashMap<String,Object>> OnBeforeOnFindRecord(List<LinkedHashMap<String,Object>> Records) {
        return Records;
    }

    @OnAfterGetRecord
    public Customer OnBeforeOnAfterGetRecord(BusinessCentralRecord<Customer,Customer.Fields> Rec) throws Exception {
        Rec.Validate(Customer.Fields.phoneNumber,"Irina",true);
        Rec.Modify(true);
        return Rec.GetRecord();
    }

    @OnNextRecord
    public Integer OnBeforeOnNextRecord(Integer Steps) {
//        Steps = 2;
        return Steps;
    }

    @OnAfterGetCurrRecord
    public Customer OnBeforeOnAfterCurrRecRecord(BusinessCentralRecord<Customer,Customer.Fields> Rec) throws Exception {
        Rec.Validate(Customer.Fields.phoneNumber,"********",true);
        Rec.Modify(true);
        return Rec.GetRecord();
    }

    @OnQueryClosePage
    public void OnBeforeOnQueryClosePage() {
    }

    @OnClosePage
    public void OnClosePage() {
    }

    @Action(NAME = "ChangeAccounStatus")
    public void SendMessage() throws Exception {
        record.Reset();
        record.FindSet();
        while (record.HasNext()) {
            record.Next();
            record.Validate(Customer.Fields.accountStatus,"Closed",false);
            record.Modify(true);

            System.out.println(record.GetRecord());
        }
    }

    @Autowired
    private CustomerRecord record;
}
