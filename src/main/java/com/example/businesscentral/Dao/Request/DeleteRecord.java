package com.example.businesscentral.Dao.Request;

import lombok.Data;
import lombok.experimental.Accessors;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@Accessors(chain = true)
public class DeleteRecord {
    private String table;
    private List<LinkedHashMap<String,Object>> records;
}
