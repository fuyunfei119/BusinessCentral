package com.example.businesscentral.Dao.Request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

@Data
@Accessors(chain = true)
public class DeleteRecord {
    private String table;
    private LinkedHashMap<String,Object> record;
}
