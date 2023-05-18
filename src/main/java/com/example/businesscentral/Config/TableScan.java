package com.example.businesscentral.Config;

import com.example.businesscentral.Annotation.Table;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(value = "com.example.businesscentral.Table",
                includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Table.class)})
public class TableScan {
}
