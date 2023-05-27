package com.example.businesscentral.Dao.Scanner;

import com.example.businesscentral.Dao.Annotation.Table;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

@ComponentScan(value = "com.example.businesscentral.Table",
                includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Table.class)})
public class BusinessCentralObjectScan {
}
