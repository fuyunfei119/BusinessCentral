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
import org.springframework.beans.factory.annotation.Autowired;


import java.util.LinkedHashMap;
import java.util.List;

@Page(SOURCETABLE = "Customer", TYPE = PageType.List)
@Data
public class CustomerList {

    @Autowired
    private CustomerRecord record;

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
//        System.out.println("OnOpenPage trigger raised...");
    }

    @OnFindRecord
    public List<LinkedHashMap<String,Object>> OnBeforeOnFindRecord(List<LinkedHashMap<String,Object>> Records) {
//        System.out.println("OnFindRecord trigger raised...");
        return Records;
    }

    @OnAfterGetRecord
    public Customer OnBeforeOnAfterGetRecord(BusinessCentralRecord<Customer,Customer.Fields> Rec) throws Exception {

//        System.out.println("OnAfterGetRecord trigger raised...");
        System.out.println(Rec.GetRecord());
        Rec.Validate(Customer.Fields.customerType,"Sievers",true);
        Rec.Modify(true);
//        Rec.setCustomerType("Sievers");
//        return Rec;
        return Rec.GetRecord();
    }

    @OnNextRecord
    public Integer OnBeforeOnNextRecord(Integer Steps) {
//        System.out.println("OnNextRecord trigger raised...");
//        Steps = 3;
//        System.out.println(Steps);
        return Steps;
    }

    @OnAfterGetCurrRecord
    public void OnBeforeOnAfterCurrRecRecord(Customer customer) {
//        System.out.println("OnAfterCurrRecRecord trigger raised...");
//        System.out.println(customer);
    }

    @OnQueryClosePage
    public void OnBeforeOnQueryClosePage() {
//        System.out.println("OnQueryClosePage trigger raised...");
    }

    @OnClosePage
    public void OnClosePage() {
//        System.out.println("OnClosePage trigger raised...");
    }
}
