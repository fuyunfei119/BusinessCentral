package com.example.businesscentral.Page;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.PageData.CustomerPageData;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import com.example.businesscentral.Table.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.*;

@Page(SOURCETABLE = "Customer", TYPE = PageType.List, Method = "FindSetProtoType")
public class CustomerPage {
    @Autowired
    private CustomerPageData customerPageData;
    @Autowired
    private BusinessCentralSystemRecord businessCentralSystemRecord;
    @Autowired
    private ApplicationContext applicationContext;

    public List<LinkedHashMap<String, Object>> FindSetProtoType() throws Exception {
        customerPageData.Reset();
        customerPageData.SetLoadFields(Customer.Fields.userId);
        customerPageData.SetLoadFields(Customer.Fields.firstName);
        customerPageData.SetLoadFields(Customer.Fields.lastName);
        customerPageData.SetLoadFields(Customer.Fields.customerType);
        customerPageData.SetLoadFields(Customer.Fields.points);
        customerPageData.SetLoadFields(Customer.Fields.phoneNumber);
        customerPageData.SetLoadFields(Customer.Fields.accountStatus);
        return customerPageData.FindSet();
    }

    public List<LinkedHashMap<String, Object>> FindSetByTableName(String TableName) throws ClassNotFoundException {
        return businessCentralSystemRecord.FindSetByTableName(TableName);
    }

    public List<Object> FindSetByFields(Map<String,Object> filters) {
        List<LinkedHashMap<String, Object>> linkedHashMaps = businessCentralSystemRecord.FindSetByFields(filters);

        List<Object> convertedList = new ArrayList<>();

        for (LinkedHashMap<String, Object> map : linkedHashMaps) {
            Object item = map.values().stream().findFirst().orElse(null);
            convertedList.add(item);
        }

        return convertedList;
    }

    public List<String> GetFilterGroups(Map<String,Object> filters) {

        List<String> fields = new ArrayList<>();

        String table = (String) filters.get("table");

        Object bean = applicationContext.getBean(table.toLowerCase(Locale.ROOT));

        for (Field declaredField : bean.getClass().getDeclaredFields()) {
            fields.add(BusinessCentralUtils.convertToSnakeCase(declaredField.getName()));
        }

        return fields;
    }


    public List<LinkedHashMap<String, Object>> FindSetByFilters(Map<String, Object> filters) throws Exception {
        return businessCentralSystemRecord.FindSetByFilters(filters);
    }

    public List<LinkedHashMap<String, Object>> QueryContent(Map<String,Object> filters) {
        return businessCentralSystemRecord.QueryContent(filters);
    }

    public List<LinkedHashMap<String, Object>> SortLinesByDescending(Map<String, Object> filters) throws Exception {
        return businessCentralSystemRecord.SortLinesByDescending(filters);
    }


}
