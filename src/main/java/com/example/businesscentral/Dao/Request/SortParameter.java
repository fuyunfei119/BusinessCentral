package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class SortParameter {
    @JsonProperty("table")
    private String table;
    @JsonProperty("filters")
    private LinkedHashMap<String,Object> filters;
    @JsonProperty("sort")
    private List<LinkedHashMap<String,Object>> sort;
}
