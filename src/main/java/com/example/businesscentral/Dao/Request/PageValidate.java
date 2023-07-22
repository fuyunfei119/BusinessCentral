package com.example.businesscentral.Dao.Request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

@Data
@Accessors(chain = true)
public class PageValidate {
    private String table;
    private String page;
    private Object currentValue;
    private Object newValue;
    private String field;
    private Integer rowIndex;
    private LinkedHashMap<String,Object> record;
}
