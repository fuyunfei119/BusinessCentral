package com.example.businesscentral.Config;

import com.example.businesscentral.Annotation.TableTrigger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(value = "com.example.businesscentral.Trigger.*",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = TableTrigger.class)})
public class TableTriggerScan {
}
