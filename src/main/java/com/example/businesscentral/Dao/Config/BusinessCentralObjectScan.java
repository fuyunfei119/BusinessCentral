package com.example.businesscentral.Dao.Config;

import com.example.businesscentral.Dao.Annotation.Table;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

@ComponentScan(value = "com.example.businesscentral.Table",
                includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Table.class)})
//@ComponentScan(value = "com.example.businesscentral.Dao.RecordData",
//                includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Repository.class)})
//@ComponentScan(value = "com.example.businesscentral.Dao.PageData",
//                includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Repository.class)})
public class BusinessCentralObjectScan {
}
