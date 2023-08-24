package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

@Data
@Accessors(chain = true)
public class NewRecord {
    @JsonProperty("table")
    private String table;
    @JsonProperty("page")
    private String page;
    @JsonProperty("record")
    private LinkedHashMap<String,Object> record;
}
