package com.example.businesscentral.Dao.Request;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class CardParameter {
    private String table;
    private String page;
    private String recordID;
    private Object record;
    private CardParameterUpToDate updatedField;
    private Object oldValue;
    private Object newValue;
    private String fieldName;
    private Boolean isNewRecord;
}
