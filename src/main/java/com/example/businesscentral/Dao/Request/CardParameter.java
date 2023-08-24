package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class CardParameter {
    @JsonProperty("table")
    private String table;
    @JsonProperty("page")
    private String page;
    @JsonProperty("recordID")
    private String recordID;
    @JsonProperty("record")
    private Object record;
    @JsonProperty("updateField")
    private CardParameterUpToDate updatedField;
    @JsonProperty("oldValue")
    private Object oldValue;
    @JsonProperty("newValue")
    private Object newValue;
    @JsonProperty("fieldName")
    private String fieldName;
    @JsonProperty("isNewRecord")
    private Boolean isNewRecord;
}
