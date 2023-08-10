package com.example.businesscentral.Dao.Scanner;

import com.example.businesscentral.Dao.Annotation.Page;
import com.example.businesscentral.Dao.Annotation.Table;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(value = "com.example.businesscentral.Page",
                includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Page.class)})
public class BusinessCentralPageScan {
}
