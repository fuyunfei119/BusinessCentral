package com.example.businesscentral.Dao.Request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

@Data
@Accessors(chain = true)
public class NewRecord {
    private String table;
    private String page;
    private LinkedHashMap<String,Object> record;
}
