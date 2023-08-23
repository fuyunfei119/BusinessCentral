package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class TableParameter {
    @JsonProperty("page")
    private String page;
    @JsonProperty("table")
    private String table;
    @JsonProperty("records")
    private List<LinkedHashMap<String,Object>> records;
    @JsonProperty("record")
    private LinkedHashMap<String,Object> record;
}
