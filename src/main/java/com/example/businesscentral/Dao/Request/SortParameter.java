package com.example.businesscentral.Dao.Request;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class SortParameter {
    private String table;
    private LinkedHashMap<String,Object> filters;
    private List<LinkedHashMap<String,Object>> sort;
}
