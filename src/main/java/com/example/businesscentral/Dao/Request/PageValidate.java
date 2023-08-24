package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

@Data
@Accessors(chain = true)
public class PageValidate {
    @JsonProperty("table")
    private String table;
    @JsonProperty("page")
    private String page;
    @JsonProperty("currentValue")
    private Object currentValue;
    @JsonProperty("newValue")
    private Object newValue;
    @JsonProperty("field")
    private String field;
    @JsonProperty("rowIndex")
    private Integer rowIndex;
    @JsonProperty("record")
    private LinkedHashMap<String,Object> record;
    @JsonProperty("newRecord")
    private Boolean newRecord;
}
