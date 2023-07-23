package com.example.businesscentral.Dao.Request;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class TableParameter {
    private String table;
    private String page;
    private List<LinkedHashMap<String,Object>> records;
    private LinkedHashMap<String,Object> record;
}
